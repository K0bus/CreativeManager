---
name: release
description: >-
  Workflow complet de release pour CreativeManager : calcul de la nouvelle version (SemVer),
  mise à jour de pom.xml, compilation et packaging du JAR avec validation Checkstyle,
  génération du changelog, création du commit de release et création du tag Git annoté.
  À utiliser dès que l'utilisateur demande de publier une nouvelle version, faire une release,
  créer un tag de release ou préparer une version de production.
---

# Workflow de Release pour CreativeManager

Ce workflow automatise et fiabilise le cycle complet de release de CreativeManager : incrémentation de version, validation qualité, build de l'artefact JAR et création du tag Git.

Consulter la [Checklist de Release](./references/release-checklist.md) pour les pré-requis détaillés.

---

## Procédure pas-à-pas pour l'Agent IA

Lors de l'activation de ce workflow, exécuter rigoureusement les étapes suivantes :

### Étape 1 : Vérification de l'état Git

1. Vérifier la branche et le statut du dépôt :
   ```bash
   git branch --show-current
   git status --short
   ```
2. Si des modifications en cours non liées à la release sont présentes, demander confirmation à l'utilisateur avant de poursuivre.

### Étape 2 : Détection & Calcul de la version

1. Extraire la version actuelle dans `pom.xml` :
   ```bash
   grep -m 1 -oP '(?<=<version>)[^<]+(?=</version>)' pom.xml
   ```
2. Récupérer les commits depuis le dernier tag Git pour déterminer le type d'incrémentation SemVer :
   ```bash
   LAST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "")
   if [ -n "$LAST_TAG" ]; then
     git log ${LAST_TAG}..HEAD --oneline
   else
     git log -n 15 --oneline
   fi
   ```
3. Déterminer la version cible :
   - **PATCH** (`x.y.Z+1`) : uniquement des correctifs (`fix:`, `style:`, `refactor:`).
   - **MINOR** (`x.Y+1.0`) : nouvelles fonctionnalités rétrocompatibles (`feat:`).
   - **MAJOR** (`X+1.0.0`) : rupture de compatibilité majeure (`BREAKING CHANGE`).
4. Si l'utilisateur n'a pas spécifié explicitement le numéro de version, lui proposer la version calculée ou lui demander de choisir.

### Étape 3 : Mise à jour de `pom.xml`

Mettre à jour la balise `<version>` principale de `pom.xml` en préservant le suffixe `-${revision}` :
```xml
<version>NOUVELLE_VERSION-${revision}</version>
```

### Étape 4 : Compilation & Contrôle Qualité

Lancer la suite de validation et le packaging du JAR :
```bash
mvn clean package checkstyle:check -Drevision=RELEASE
```
Vérifier que :
- Le build se termine avec `BUILD SUCCESS`.
- Checkstyle signale `0 Checkstyle violations`.
- Le JAR ombré final est bien généré dans `target/CreativeManager-NOUVELLE_VERSION-RELEASE.jar`.

> **Note :** Le script d'aide [release.sh](./scripts/release.sh) peut être exécuté pour automatiser les étapes 3 et 4 :
> ```bash
> .agents/skills/release/scripts/release.sh [NOUVELLE_VERSION|patch|minor|major]
> ```

### Étape 5 : Création du Commit et du Tag Git

1. Indexer `pom.xml` et les éventuels fichiers de documentation mis à jour :
   ```bash
   git add pom.xml
   ```
2. Créer le commit conventionnel de release :
   ```bash
   git commit -m "chore: bump project version to NOUVELLE_VERSION"
   ```
3. Créer le tag Git annoté :
   ```bash
   git tag -a "vNOUVELLE_VERSION" -m "Release NOUVELLE_VERSION"
   ```

### Étape 6 : Synthèse de la Release

Présenter à l'utilisateur :
1. Le **numéro de version** final publié et le **tag Git** (`vX.Y.Z`).
2. L'emplacement de l'artefact compilé (`target/CreativeManager-X.Y.Z-RELEASE.jar`).
3. Le **Changelog** synthétique regroupant les commits intégrés dans la version.
4. La commande pour pousser les modifications et le tag vers GitHub :
   ```bash
   git push origin HEAD --tags
   ```
