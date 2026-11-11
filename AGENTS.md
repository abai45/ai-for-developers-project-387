# AGENTS.md

Учебный проект «Календарь звонков»: сервис бронирования встреч (клон Cal.com без авторизации).
Владелец заводит типы встреч, гость без регистрации записывается на свободный слот.

## Устройство проекта

- `backend/` — Spring Boot 4.1, Java 21, Maven; дымовой эндпоинт `GET /api/health`
- `frontend/` — React 19 + Vite + TypeScript + Tailwind CSS 4 + shadcn/ui; проксирует `/api` на бекенд
- `docs/decisions.md` — архитектурные решения: параметры расписания, модель данных, эндпоинты, стек
- `docs/adr/` — ключевые решения в формате ADR (статус, контекст, решение, последствия, ссылка на decisions.md)
- Контрактная цепочка: TypeSpec-исходники `docs/spec/tsp/` → `mise run spec` → сгенерированные `docs/openapi.json` и `frontend/src/lib/api/schema.d.ts` (Java-артефакты openapi-generator — в `backend/target/`, не коммитятся)
- Хранилище — in-memory (в памяти процесса; PostgreSQL запланирован отдельным шагом)

## Команды

- `mise run setup` — установить зависимости бекенда и фронтенда
- `mise run spec` — генерация контракта: TypeSpec → OpenAPI → TS-типы
- `mise run check` — все проверки: дрейф-чек контракта, тесты, линт, типы, сборка, браузерный E2E (та же команда в CI)
- `mise run e2e` — браузерные сценарии Playwright против настоящего сервера (сам поднимает бекенд :8080 и фронтенд :5173)
- `mise run backend` — бекенд на :8080
- `mise run frontend` — фронтенд на :5173
- `mise run backend-test` / `mise run frontend-test` — тесты по частям
- `mise daemons start` / `mise daemons stop` — бекенд и фронтенд фоном (pitchfork-демоны из `[daemons]`); `mise daemons status` / `logs` — состояние и логи

## Правила

- Коммиты строго по Conventional Commits: `feat:`, `fix:`, `docs:`, `chore:`, `test:`, `refactor:`, `ci:`.
- Коммит реализации ссылается на тикет, который он закрывает: `feat: запись на свободный слот (#12)`. GitHub превращает номер в ссылку, по истории видно, какой коммит какую задачу делал.
- Архитектурные решения фиксируются в `docs/decisions.md` до реализации.
- Релизы — через release-please (`.github/workflows/release-please.yml`): push в `main` создаёт/обновляет release-PR по Conventional Commits, релиз публикуется мержем этого PR; теги и версии вручную не двигаем.
- Сгенерированные артефакты не правятся руками: `docs/openapi.json` и `frontend/src/lib/api/schema.d.ts` изменяются только через `mise run spec` (правка TypeSpec → генерация → код обеих сторон). CI пересобирает генерацию и ловит дрейф (`mise run spec-check`).
- `mise run check` должен быть зелёным до каждого push; проверки не редактируются под задачу.

## Agent skills

### Issue tracker

Issues живут в GitHub Issues этого репозитория (`abai45/ai-for-developers-project-387`), всё через `gh` CLI. See `docs/agents/issue-tracker.md`.

### Domain docs

Single-context: `GLOSSARY.md` в корне, решения — в `docs/adr/`. See `docs/agents/domain.md`.
