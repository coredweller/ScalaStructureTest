# Project: ScalaStructureTest

## Overview
A Scala 3 Play Framework application with Cats Effect IO, compile-time DI, and functional domain modeling.

## Tech Stack
- **Language**: Scala 3.3.4
- **Framework**: Play Framework 3.0.5
- **Effects**: Cats Effect 3.5.4 (IO)
- **Streaming**: fs2 3.11.0
- **Refined Types**: iron 2.6.0
- **Testing**: ScalaTest + cats-effect-testing-scalatest
- **Build**: sbt 1.10.7

## Key Design Decisions
- **Compile-time DI** via `AppLoader` + `AppComponents` — no Guice, no runtime reflection
- **IO everywhere** — `Future` only at controller boundary via `unsafeToFuture()`
- **Scala 3 syntax** — `given`/`using`, `enum`, opaque types (no `implicit def`)
- **Play JSON** with `given Format[T]` for serialization

## Common Commands
```bash
sbt run                    # Start dev server (port 9000)
sbt ~run                   # Watch mode with hot reload
sbt test                   # Run all tests
sbt compile                # Compile only
sbt scalafmtAll            # Format all sources
sbt scalafmtCheckAll       # Check formatting
sbt stage                  # Stage for Docker
```

## Code Conventions
- Load the `scala-play` skill when working in this codebase
- See `.claude/skills/scala-play/` for patterns, templates, and references
- Use `/scaffold-scala-play` to scaffold new components
- Use `/review-code` for code review, `/audit-security` for security audit

## Git Workflow
- Branch naming: `feature/<ticket>-<description>`, `bugfix/<ticket>-<description>`
- Commit messages: conventional commits (`feat:`, `fix:`, `docs:`, `refactor:`)
- Always create PR — no direct push to `develop`

## Important Rules
- Never commit secrets — use environment variables or `.env` files
- Always write tests for new features
- Run `sbt compile` with `-Xfatal-warnings` before submitting (zero warnings required)
