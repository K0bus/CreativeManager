#!/usr/bin/env bash
# ==============================================================================
# CreativeManager - Release Helper Script
# Usage:
#   ./release.sh [next_version|patch|minor|major] [--dry-run] [--no-tag]
# Examples:
#   ./release.sh patch
#   ./release.sh 1.35.24
#   ./release.sh 1.35.24 --dry-run
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/../../../../" && pwd)"
POM_FILE="${ROOT_DIR}/pom.xml"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1" >&2; }

# Parse arguments
TARGET_ARG="${1:-patch}"
DRY_RUN=false
CREATE_TAG=true

for arg in "$@"; do
  case "$arg" in
    --dry-run) DRY_RUN=true ;;
    --no-tag) CREATE_TAG=false ;;
  esac
done

cd "${ROOT_DIR}"

# 1. Extract current base version from pom.xml
if [[ ! -f "${POM_FILE}" ]]; then
  log_error "pom.xml introuvable dans ${ROOT_DIR}"
  exit 1
fi

CURRENT_VERSION_RAW=$(grep -m 1 -oP '(?<=<version>)[^<]+(?=</version>)' "${POM_FILE}" || true)
if [[ -z "${CURRENT_VERSION_RAW}" ]]; then
  log_error "Impossible d'extraire la version depuis pom.xml"
  exit 1
fi

# Remove -${revision} if present to get the clean SemVer
BASE_VERSION="${CURRENT_VERSION_RAW%-\$\{revision\}*}"
log_info "Version actuelle détectée : ${BASE_VERSION} (raw: ${CURRENT_VERSION_RAW})"

# 2. Determine target version
IFS='.' read -r MAJOR MINOR PATCH <<< "${BASE_VERSION}"

case "${TARGET_ARG}" in
  patch)
    PATCH=$((PATCH + 1))
    TARGET_VERSION="${MAJOR}.${MINOR}.${PATCH}"
    ;;
  minor)
    MINOR=$((MINOR + 1))
    PATCH=0
    TARGET_VERSION="${MAJOR}.${MINOR}.${PATCH}"
    ;;
  major)
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    TARGET_VERSION="${MAJOR}.${MINOR}.${PATCH}"
    ;;
  *)
    # Custom version passed
    TARGET_VERSION="${TARGET_ARG}"
    ;;
esac

log_info "Nouvelle version ciblée : ${TARGET_VERSION}"

if [[ "${DRY_RUN}" == "true" ]]; then
  log_warn "Mode --dry-run activé : aucune modification ne sera enregistrée."
fi

# 3. Update pom.xml
NEW_RAW_VERSION="${TARGET_VERSION}-\${revision}"
log_info "Mise à jour de pom.xml vers <version>${NEW_RAW_VERSION}</version>..."

if [[ "${DRY_RUN}" == "false" ]]; then
  # Use sed to update the main project version (first occurrence after artifactId)
  sed -i "0,/<version>.*<\/version>/s/<version>.*<\/version>/<version>${NEW_RAW_VERSION}<\/version>/" "${POM_FILE}"
fi

# 4. Build & Checkstyle validation
log_info "Exécution du build Maven & vérification Checkstyle (mvn clean package checkstyle:check)..."
if [[ "${DRY_RUN}" == "false" ]]; then
  mvn clean package checkstyle:check -Drevision=RELEASE
fi
log_success "Build & validation Maven terminés avec succès."

# 5. Git commit & tag
if [[ "${DRY_RUN}" == "false" ]]; then
  log_info "Ajout des fichiers modifiés à git..."
  git add "${POM_FILE}"
  
  COMMIT_MSG="chore: bump project version to ${TARGET_VERSION}"
  git commit -m "${COMMIT_MSG}"
  log_success "Commit créé : ${COMMIT_MSG}"

  if [[ "${CREATE_TAG}" == "true" ]]; then
    TAG_NAME="v${TARGET_VERSION}"
    # Check if tag already exists
    if git rev-parse "${TAG_NAME}" >/dev/null 2>&1; then
      log_warn "Le tag ${TAG_NAME} existe déjà. Création annulée."
    else
      git tag -a "${TAG_NAME}" -m "Release ${TARGET_VERSION}"
      log_success "Tag Git créé : ${TAG_NAME}"
    fi
  fi
fi

# 6. Summary
echo ""
echo "================================================================="
log_success "Workflow de release terminé pour ${TARGET_VERSION} !"
echo "Artefact produit : target/CreativeManager-${TARGET_VERSION}-RELEASE.jar (ou -SNAPSHOT.jar)"
echo "Pour pousser sur le dépôt distant :"
echo "  git push origin HEAD --tags"
echo "================================================================="
