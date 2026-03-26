# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SmartEngine is a lightweight BPMN 2.0 compliant process orchestration engine by Alibaba. It supports two execution modes: **Custom Mode** (embedded, in-memory) and **Database Mode** (MySQL/PostgreSQL via MyBatis). Java 8+ required; CI tests against JDK 8 and 17.

## Build & Test Commands

```bash
# Full build with tests
mvn -q -DskipTests=false test

# Compile only (no tests)
mvn -B -ntp -DskipTests package

# Run a specific module's tests
mvn -pl core -am test
mvn -pl extension/storage/storage-custom -am test
mvn -pl extension/storage/storage-mysql -am test

# Run DB-independent tests only (what CI runs)
mvn -B -ntp -pl core,extension/storage/storage-common,extension/storage/storage-custom,extension/retry/retry-common,extension/retry/retry-custom -am test

# Run a single test class
mvn -pl core -am -Dtest=ServiceTaskTest test

# Run a single test method
mvn -pl core -am -Dtest=ServiceTaskTest#testServiceTask test
```

Test framework is JUnit 4 + JUnit 5 (Jupiter with Vintage). Database tests use H2 by default.

## Module Architecture

```
SmartEngine
├── core/                              # Engine kernel: BPMN parser, PVM execution, service APIs
├── extension/
│   ├── storage/
│   │   ├── storage-common/            # Storage abstractions and interfaces
│   │   ├── storage-custom/            # In-memory stub implementation (semantic regression tests)
│   │   ├── storage-mysql/             # MyBatis-based relational storage (MySQL/PostgreSQL)
│   │   └── storage-dual/             # Hybrid dual-storage mode
│   ├── retry/
│   │   ├── retry-common/             # Retry abstractions
│   │   ├── retry-custom/             # Custom retry implementation
│   │   └── retry-mysql/             # Persistent retry via MySQL
│   └── archive/
│       └── archive-mysql/            # Historical data archival
└── docs/                             # Comprehensive documentation (intro, concepts, API, persistence, dev)
```

**Dependency flow:** `core` has no storage dependency. Storage modules depend on `storage-common` which depends on `core`. The engine uses a `StorageRouter` abstraction so storage implementations are pluggable.

## Key Design Patterns

- **CQRS services:** Command services (`ProcessCommandService`, `TaskCommandService`, etc.) for writes; Query services (`ProcessQueryService`, `TaskQueryService`, etc.) for reads. All accessed through the `SmartEngine` interface.
- **Extension binding:** `@ExtensionBinding` annotation registers BPMN element behaviors. `SimpleAnnotationScanner` discovers them on classpath.
- **PVM (Process Virtual Machine):** Core execution model with Process/Execution/Activity/Token hierarchy in `core/.../pvm/`.
- **Expression evaluation:** MVEL is the default expression language for sequence flow conditions.

## Key Entry Points

- `SmartEngine` interface: main API, provides all command/query services
- `DefaultSmartEngine`: default implementation, wired via `ProcessEngineConfiguration`
- BPMN parsing: `core/.../bpmn/assembly/` (XML to model) and `core/.../bpmn/behavior/` (element behaviors)
- Test BPMN definitions: `*/src/test/resources/*.bpmn20.xml`

## Conventions

- Java 8 compatibility is required — no `Map.of()`, `List.of()`, or other Java 9+ APIs
- Commit messages use present tense describing the behavior change
- DB changes must update both MySQL and PostgreSQL DDL, MyBatis SQL maps, and provide migration notes
- Dual-language changelogs: update both `CHANGELOG.md` and `CHANGELOG-zh.md`
- DDL reference: `docs/04-persistence/database-schema.md`
