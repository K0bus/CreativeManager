# Checklist de Release CreativeManager

Ce document décrit les vérifications à effectuer avant et après chaque publication de version de CreativeManager.

---

## 1. Pré-requis Git & Environnement

- [ ] La branche courante est `master` (ou la branche de release cible).
- [ ] La branche locale est à jour avec le dépôt distant (`git fetch && git status`).
- [ ] Aucun fichier indésirable n'est non-versionné ou temporaire (`git status`).
- [ ] Les plugins nécessaires sont correctement configurés dans `plugin.yml` (`softdepend`, permissions, etc.).

---

## 2. Règles de Nommage et Versioning

Le projet utilise Semantic Versioning (`MAJOR.MINOR.PATCH`) combiné avec le mécanisme `${revision}` de Maven :
- `<version>1.35.X-${revision}</version>`
- `<revision>SNAPSHOT</revision>`

### Règles de bump SemVer :
1. **PATCH (`+0.0.1`)** : corrections de bugs, patchs de compatibilité, refactoring interne sans ajout majeur. Ex: `1.35.23` -> `1.35.24`.
2. **MINOR (`+0.1.0`)** : nouvelles fonctionnalités, nouveaux modules, support d'une version majeure de Minecraft/Spigot rétrocompatible. Ex: `1.35.24` -> `1.36.0`.
3. **MAJOR (`+1.0.0`)** : refonte de l'API, refonte majeure des configurations ou rupture de compatibilité. Ex: `1.35.24` -> `2.0.0`.

### Format du Tag Git :
- Tag annoté : `v1.35.X` ou `1.35.X` (recommandé avec le préfixe `v`, ex: `v1.35.24`).
- Message : `Release 1.35.X`

---

## 3. Contrôle Qualité & Build

Avant de poser un tag Git, toujours valider :
1. Compilation complète : `mvn clean compile`
2. Contrôle Checkstyle : `mvn checkstyle:check` (0 violation requise).
3. Packaging JAR : `mvn clean package -Drevision=RELEASE`
4. Vérification de l'artefact généré : `target/CreativeManager-<version>-RELEASE.jar` (présence du jar ombré avec les dépendances internes relocalisées).

---

## 4. Post-Release

- [ ] Vérifier que le tag local est bien créé : `git tag -l -n1 v<version>`.
- [ ] Pousser le commit et le tag :
  ```bash
  git push origin HEAD --tags
  ```
- [ ] (Optionnel) Publier la release sur GitHub Releases avec le changelog généré et le JAR attaché.
