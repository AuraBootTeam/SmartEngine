# Rename com.alibaba to com.auraboot Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rename the project's Maven groupId and Java package from `com.auraboot.smart.framework` to `com.auraboot.smart.framework`, and add fork explanation to README.

**Architecture:** Mechanical rename across 840 files. The safe replacement pattern is `com.auraboot.smart` → `com.auraboot.smart` (dot form) and `com/auraboot/smart` → `com/auraboot/smart` (path form). This avoids touching third-party `com.alibaba.fastjson` references. Directory renames use `git mv` to preserve history.

**Tech Stack:** Maven, Java 8, MyBatis, Spring XML, BPMN XML

**Key safety rule:** Never replace bare `com.alibaba` — always require `.smart` or `/smart` suffix to avoid renaming fastjson dependency references.

---

### Task 1: Rename Java directory structure (13 directories)

**Files:** All `src/main/java/com/alibaba` and `src/test/java/com/alibaba` directories across all modules.

- [ ] **Step 1: Rename all 13 directories using git mv**

```bash
cd /Users/ghj/work/fork/SmartEngine

# For each module with src/main/java
for dir in \
  core/src/main/java \
  core/src/test/java \
  extension/archive/archive-mysql/src/main/java \
  extension/archive/archive-mysql/src/test/java \
  extension/retry/retry-mysql/src/main/java \
  extension/retry/retry-custom/src/main/java \
  extension/retry/retry-custom/src/test/java \
  extension/retry/retry-common/src/main/java \
  extension/storage/storage-custom/src/main/java \
  extension/storage/storage-custom/src/test/java \
  extension/storage/storage-common/src/main/java \
  extension/storage/storage-mysql/src/main/java \
  extension/storage/storage-mysql/src/test/java; do
  if [ -d "$dir/com/alibaba" ]; then
    mkdir -p "$dir/com/auraboot"
    git mv "$dir/com/auraboot/smart" "$dir/com/auraboot/smart"
    rmdir "$dir/com/alibaba" 2>/dev/null || true
  fi
done
```

- [ ] **Step 2: Verify no com/alibaba directories remain in src trees**

Run: `find . -path '*/src/*/java/com/alibaba' -type d`
Expected: no output

- [ ] **Step 3: Commit**

```bash
git add -A
git commit -m "refactor: rename directory structure com/alibaba → com/auraboot"
```

### Task 2: Replace all text references in file contents

**Files:** All `.java`, `.xml`, `.md`, `.properties`, `pom.xml` files across the project.

- [ ] **Step 1: Replace dot-form package references**

```bash
# Replace com.auraboot.smart → com.auraboot.smart in ALL files
find . -type f \( -name '*.java' -o -name '*.xml' -o -name '*.md' -o -name '*.properties' -o -name '*.yml' -o -name '*.yaml' \) \
  -exec sed -i '' 's/com\.alibaba\.smart/com.auraboot.smart/g' {} +
```

- [ ] **Step 2: Replace path-form references**

```bash
# Replace com/auraboot/smart → com/auraboot/smart in ALL files (MyBatis, Spring XML, etc.)
find . -type f \( -name '*.java' -o -name '*.xml' -o -name '*.md' -o -name '*.properties' \) \
  -exec sed -i '' 's|com/auraboot/smart|com/auraboot/smart|g' {} +
```

- [ ] **Step 3: Verify no project references remain (only fastjson should remain)**

Run: `grep -r "com\.alibaba" --include='*.java' --include='*.xml' --include='*.md' --include='*.properties' . | grep -v fastjson | grep -v '.git/'`
Expected: empty (no matches) or only non-project references

- [ ] **Step 4: Verify fastjson references are untouched**

Run: `grep -r "com\.alibaba\.fastjson\|<groupId>com.alibaba</groupId>" --include='*.java' --include='*.xml' .`
Expected: 3 matches (2 pom.xml fastjson deps + 1 Java import)

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "refactor: rename package com.auraboot.smart → com.auraboot.smart in all file contents"
```

### Task 3: Update README with fork explanation

**Files:**
- Modify: `README.md`
- Modify: `README-zh.md`

- [ ] **Step 1: Add fork notice to README.md**

Add after the first heading, before "Design Philosophy":

```markdown
> **Note:** This is a fork of [alibaba/SmartEngine](https://github.com/alibaba/SmartEngine), maintained by the original author. This fork exists because the original repository cannot publish releases to Maven Central. The Maven coordinates have been changed from `com.auraboot.smart.framework` to `com.auraboot.smart.framework`.
```

- [ ] **Step 2: Add fork notice to README-zh.md**

Add after the first heading, before "设计理念":

```markdown
> **说明：** 本仓库是 [alibaba/SmartEngine](https://github.com/alibaba/SmartEngine) 的 fork，由原作者维护。由于无法在原仓库将 JAR 发布到 Maven 中央仓库，因此创建了此 fork。Maven 坐标已从 `com.auraboot.smart.framework` 变更为 `com.auraboot.smart.framework`。
```

- [ ] **Step 3: Commit**

```bash
git add README.md README-zh.md
git commit -m "docs: add fork explanation to README"
```

### Task 4: Build verification

- [ ] **Step 1: Run full build with tests**

```bash
mvn -q -DskipTests=false test
```

Expected: BUILD SUCCESS

- [ ] **Step 2: If build fails, fix any missed references and re-run**

- [ ] **Step 3: Final commit if any fixes were needed**

```bash
git add -A
git commit -m "fix: correct remaining references after package rename"
```
