# Календарь звонков (продолжение)


[![hexlet-check](https://github.com/abai45/ai-for-developers-project-387/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/abai45/ai-for-developers-project-387/actions)

Разработайте совместно с ИИ сервис для бронирования календаря

Учебный проект Хекслета: https://ru.hexlet.io/programs/ai-for-developers
Как это должно работать: https://files.hexlet.app/a/2ipc5m

## Публичная ссылка

**https://calendar-zvonkov.onrender.com** — живой сервис (Render, бесплатный план: спит без запросов, первый отклик несколько секунд).

Там же можно проверить сквозной сценарий: владелец заводит тип, гость записывается, пересекающаяся запись отклоняется с понятным сообщением.

Если ссылка не открывается: сервис на бесплатном плане спит без запросов, первый отклик занимает до ~30 секунд — обновите страницу. Запасной вариант — локальный запуск (см. [Установка](#установка)) или Docker (см. [Docker](#docker)).

## Исходный проект

Этот репозиторий — автоматизация процесса разработки вокруг приложения из первого проекта «Календарь звонков». История решений сохранена в исходном репозитории:

- Репозиторий первого проекта: https://github.com/abai45/ai-for-developers-project-386
- Согласованная спецификация: [issue #1 «Карта: согласованная спецификация»](https://github.com/abai45/ai-for-developers-project-386/issues/1) и [issue #6 «Спецификация: Календарь звонков»](https://github.com/abai45/ai-for-developers-project-386/issues/6)
- Тикеты и pull request первого проекта: https://github.com/abai45/ai-for-developers-project-386/issues?q=
- Решения: [docs/decisions.md](docs/decisions.md) и [docs/adr/](docs/adr/) (копия перенесена сюда; исходники — в [386/docs/decisions.md](https://github.com/abai45/ai-for-developers-project-386/blob/main/docs/decisions.md) и [386/docs/adr/](https://github.com/abai45/ai-for-developers-project-386/tree/main/docs/adr))
- Словарь проекта: [GLOSSARY.md](GLOSSARY.md)

В этом репозитории живёт трекер новых задач ([GitHub Issues](https://github.com/abai45/ai-for-developers-project-387/issues)), воркфлоу и агенты; адрес трекера обновлён в [AGENTS.md](AGENTS.md) и [docs/agents/issue-tracker.md](docs/agents/issue-tracker.md).

## Агент в GitHub (OpenCode)

Локально агент работает с вашей рабочей копией. Здесь событие репозитория запускает его внутри GitHub Actions, а ответ появляется комментарием от имени приложения.

**Что установлено.** На репозиторий установлен [GitHub App opencode-agent](https://github.com/apps/opencode-agent) — он даёт агенту права на комментарии и push через OIDC-обмен (`id-token: write`), а `GITHUB_TOKEN` воркфлоу остаётся только на чтение. **Отличие от другого способа:** можно обойтись без App, передав в воркфлоу свой токен (`use_github_token: true`) — тогда права на запись и push настраиваются вручную через `GITHUB_TOKEN`, и это не тот же сценарий «агента приложения»; здесь выбран стандартный режим с App.

**Воркфлоу:** все замержены в `main` — интерактивный [.github/workflows/opencode.yml](.github/workflows/opencode.yml) и автотриаж [.github/workflows/opencode-triage.yml](.github/workflows/opencode-triage.yml) (ниже), цикл ревью (подраздел «Цикл ревью pull request») и ежедневный аудит (подраздел «Ежедневный аудит Lighthouse»).

| Параметр | Значение |
| --- | --- |
| Событие | `issue_comment` и `pull_request_review_comment`, `types: [created]` — правка комментария не перезапускает агента |
| Команды (mentions) | набор по умолчанию: `/opencode` и `/oc`; параметр `mentions` не задан — записано явно здесь |
| Модель | `opencode/mimo-v2.6-flash-free` (провайдер OpenCode, Console «Hexlet Students»), ключ — только в Secrets |
| Ключ | `OPENCODE_API_KEY` в Settings → Secrets and variables → Actions; в git не хранится |
| share | `false` — сессии агента не публикуются; решение принято для открытого репозитория до первого вызова |
| Назначение | ответы на команду в issue/PR, чтение обсуждения, при необходимости — правки в той же ветке |
| Результаты | комментарий агента в issue/PR и прогон во вкладке **Actions → opencode** |

**Фильтры до запуска модели.** Комментарий запускает работу только если: в тексте есть команда; автор — `OWNER`/`MEMBER`/`COLLABORATOR` (недоверенные участники не получают секреты); автор не бот (комментарии ботов игнорируются, чтобы агент не отвечал на свои сообщения). PR из fork не запускаются вовсе — ключ модели им недоступен; `pull_request_target` с исполнением кода чужого PR не используется.

**Права и надёжность.** Стандартный режим: `contents: read`, `id-token: write`, `persist-credentials: false` у checkout (git-доступ даёт токен App). Дополнительно: `pull-requests: read` (проверка, не fork ли PR) и `issues: write` (триаж — `gh` из сессии читает обращения и вешает метки `needs-triage`/`enhancement`/`spec`/`ticket`). В [opencode.json](opencode.json) `permission.external_directory: "allow"` — без этого headless-сессия на раннере (opencode v1.19.x) зависает на запросе разрешения прочитать путь вне репозитория (например, event-payload `$GITHUB_EVENT_PATH`, где лежат текущие метки) и умирает по `timeout-minutes: 15`. У задания `timeout-minutes: 15`, `concurrency` группирует прогоны по номеру issue/PR с `cancel-in-progress: true` — новый вызов останавливает текущий прогон.

**Скиллы на чистом раннере.** Проектные скиллы (`implement-spec`, `setup-matt-pocock-skills`) лежат в [.agents/skills/](.agents/skills/) и едут с репозиторием; глобальные скиллы первой проектной сессии (`to-spec`, `to-tickets`) ставятся шагом воркфлоу через `skills` CLI. Настройки трекера — в [docs/agents/](docs/agents/), правила проекта — в [AGENTS.md](AGENTS.md): они читаются агентом из репозитория.

**Вызов:** напишите в issue или PR комментарий вида `/oc explain this issue` — агент прочитает обсуждение и ответит тем же комментарием. Каждый вызов расходует токены; бюджет, если провайдер его позволяет, задаётся в кабинете провайдера. Описание воркфлоу дополняется при добавлении следующих.

### Автотриаж входящих обращений (opencode-triage.yml)

Новые обращения разбираются автоматически, без команды человека — у события нет комментария, поэтому инструкция лежит в `prompt:` воркфлоу.

| Параметр | Значение |
| --- | --- |
| Событие | `issues`, `types: [opened]` — правка и смена меток не перезапускают разбор |
| Prompt | «запусти скилл triage на новом issue»: прочитать обращение, проверить по коду/контракту/глоссарию, что уже умеет приложение, задать уточняющие вопросы, проставить метки по словарю `docs/agents/issue-tracker.md`; код не менять, PR не открывать, `to-spec`/`to-tickets` не запускать |
| Модель, ключ, share | те же: `opencode/mimo-v2.6-flash-free`, `OPENCODE_API_KEY` из Secrets, `share: false`, `mentions` не участвуют (событие без комментария) |
| Назначение | предварительный разбор входящих обращений; результаты — комментарий агента в issue и прогон в **Actions → opencode-triage** |
| Скиллы на раннере | шаг ставит `triage` (глобальный из `mattpocock/skills`); проектные едут с репозиторием |

**Фильтры до запуска модели.** Прогон создаётся, только если: автор обращения — `OWNER`/`MEMBER`/`COLLABORATOR` (недоверенные не получают секреты); отправитель не бот; у issue нет меток `spec`/`ticket`/`ready-for-agent` — тикеты, подготовленные `to-spec`/`to-tickets`, повторного триажа не требуют и проходят как `skipped`. **Права:** `contents: read`, `id-token: write`, `issues: write` (метки через `gh` из сессии), `persist-credentials: false`. **Надёжность:** `timeout-minutes: 15`, `concurrency` по номеру issue на уровне задания.

Один и тот же воркфлоу-файл не совмещает оба режима: у комментария команда в тексте, у `issues` команда задаётся `prompt` — разные фильтры, поэтому события разведены по файлам.

### Цикл ревью pull request (opencode-review.yml, verify-head.yml, pr-template-check.yml)

От issue к PR и ревью: реализация через `implement-spec` (комментарий `/oc` в исходном issue) → независимое авторевью → человеческие замечания → правки агента в той же ветке → решение о мерже человеком.

**[opencode-review.yml](.github/workflows/opencode-review.yml)** — независимый ревьюер на событии `pull_request` (`opened`, `ready_for_review`):
- запускается на ветках своего репозитория; автор доверенный **или** это PR агента `opencode-agent[bot]` (фильтр всех Bot отключил бы ревью агентских PR); черновики (draft) не ревьюятся — ревью приходит, когда PR помечен ready;
- job `review` — обычный CLI `opencode run` (без OIDC и без App-обмена), права только на чтение (`contents/issues/actions/checks/statuses/pull-requests: read`), критерии — `REVIEW.md` из **базы** PR (ревьюимый diff не может ослабить собственные правила), `timeout-minutes: 20`, `concurrency` по номеру PR отменяет устаревшее ревью;
- job `publish` — отдельная задача без секрета модели: публикует текст ревью из артефакта командой `gh pr comment` (`pull-requests: write`); при сбое ревью публикует статус `review_failed` вместо молчания.
- Правки по замечаниям ревьюер сам не запускает — только команда человека `/oc` в обсуждении или к строке diff (последняя приходит с путём, строкой и контекстом).

**[verify-head.yml](.github/workflows/verify-head.yml)** — проверка рабочей версии без модели: `pull_request` (`opened`, `synchronize`, `reopened`, `ready_for_review`), checkout **точного `github.event.pull_request.head.sha`** (не merge-коммита), фактический SHA в step summary, полный e2e с `--trace on` (успешные прогоны тоже сохраняют trace), артефакт `verify-head-<sha>`; новый push отменяет проверку устаревшей версии. Перед мержем человек сверяет SHA артефакта с head PR.

**[pr-template-check.yml](.github/workflows/pr-template-check.yml)** — детерминированная защита класса «незаполненное тело PR» (замечание 1 ревью PR #21): на `pull_request` (`opened`, `synchronize`, `reopened`) требует пять секций шаблона с непустым содержимым — правка тела без нового коммита (`edited`) чек не перезапускает, исправленное тело подтверждается следующим push или `Re-run`; `pull-requests: read`, `timeout-minutes: 5`, без модели и секретов; ветка release-please исключена.

**Предел запусков на PR** ([opencode.yml](.github/workflows/opencode.yml), шаг «Предел запусков»): до вызова модели счётчик по истории Actions (`actions: read`) и человеческим `/oc`-комментариям треда считает допущенные запросы (Re-run, отмены и таймауты тоже попытки), лимит — 3 на тред. При исчерпании модель не вызывается, шаг без модели публикует `limit_exhausted` с причиной, попытками, head SHA и ссылкой на логи; отдельно публикуются `infra` (сбой подготовки/гейта), `agent_failed` (ошибка агента) и `timeout` (снятие прогона). Новый PR/issue получает новый бюджет. Источник счёта токен агента не перезаписывает; тела комментариев ревью на бюджет не влияют.

### Ежедневный аудит Lighthouse (lighthouse-audit.yml)

Раз в сутки приложение проверяется Lighthouse, а агент OpenCode превращает находки отчёта в issue — с дедупликацией, чтобы трекер не плодил повторы.

| Параметр | Значение |
| --- | --- |
| События | `schedule` + `workflow_dispatch` (ручной запуск из Actions после мержа в `main`) |
| Cron | `17 3 * * *` — GitHub считает cron в **UTC**, это 08:17 по времени Алматы (UTC+5), начало рабочего дня |
| Модель, ключ, share | те же: `opencode/mimo-v2.6-flash-free`, `OPENCODE_API_KEY` из Secrets, `share: "false"`; инструкция — в `prompt` (у событий нет комментария-команды) |
| Права | ровно `contents: read`, `id-token: write`, `issues: write` (агент создаёт/переоткрывает issue через `gh`); `persist-credentials: false` |
| Надёжность | `timeout-minutes: 20`; `concurrency: { group: lighthouse-audit, cancel-in-progress: false }` — новый запуск не отменяет идущий аудит |
| Результат | **артефакт `lighthouse-report`** прогона (json+html-отчёт, `retention-days: 30`, каталог в `.gitignore`) + **issues с меткой `lighthouse-audit`**: заголовок `[Lighthouse] <id>: <суть>`, внутри — проблема, действие, APP_URL, score и ссылка на прогон |

Порядок шагов: checkout → **прогрев Render** (`curl --retry 10 --retry-delay 10` на `/api/health`; бесплатный сервис спит до 30 с — при недоступности шаг падает с явным «Приложение недоступно» и до анализа дело не доходит, чтобы сбой не выглядел как «проблем нет») → `npx lighthouse@13.6.0` (версия запинена: прогоны сравнимы между собой, непроверенный релиз пакета не исполняется) → артефакт (`if: always()`) → агент.

Логика находок в prompt: аудиты с `score < 0.9`, у которых есть причина и действие (информационные без оценки пропускаются); перед созданием issue — поиск дубля `gh issue list --label lighthouse-audit --state all --search "<id>"`; **открытый** дубль — молчать, **закрытый + находка вернулась** — `gh issue reopen` + комментарий со ссылкой на свежий прогон (регрессия не потеряется). Пусто без находок — одна строка «Новых находок нет» в логе.

Успешный первый прогон (ручной dispatch): [run 43600944531](https://github.com/abai45/ai-for-developers-project-387/actions/runs/43600944531) — 11 находок → issue #28–#38 (контраст футера, robots.txt/SPA-fallback, cache TTL, неиспользуемый JS и др.).

## Как это работает

Приложение рассчитано на один сценарий: владелец календаря заранее задан и заводит типы встреч, гость без регистрации записывается на свободный слот.

### Сценарий: владелец заводит тип встречи

1. Владелец открывает главную страницу и в футере жмёт ссылку **«Владелец»** → `/owner`.
2. В форме «Создать тип встречи» заполняет **название**, **описание**, **длительность** и **шаг слота** (чипы быстрого выбора 15/30/45/60/90, дефолт 30/15, живое превью первых слотов) и сохраняет.
3. Тип появляется в списке «Типы встреч», а форма очищается со всплывающим подтверждением.

### Сценарий: запись гостя на встречу

1. Гость без регистрации заходит на сайт и нажимает **«Начать работу»** → мастер записи `/book`.
2. Выбирает нужный тип встречи из списка (шаг 1 «Тип»).
3. На шаге «Календарь» открывается окно записи на ближайшие **14 дней**: рабочие часы 09:30–18:00 (Asia/Almaty), шаг стартов равен настройкам типа, длительность слота равна длительности типа. Дни вне окна и без свободных слотов некликабельны.
4. Гость выбирает день и свободный слот, на шаге «Имя» указывает имя, на «Подтверждение» сверяет сводку и подтверждает запись.
5. Повторная запись на занятое время невозможна: сервис отвечает понятным сообщением и возвращает к календарю с обновлёнными слотами — правило проверяется **на сервере**, поэтому его нельзя обойти прямым запросом к API, в том числе для встречи другого типа.
6. Экран **«Вы записаны»** подтверждает запись; встреча сразу появляется в списке «Предстоящие встречи» у владельца.

> **Ограничение:** данные хранятся в памяти процесса (in-memory). **Перезапуск контейнера/сервера сбрасывает типы встреч и записи** — при редплое на Render данные также обнуляются. PostgreSQL запланирован отдельным шагом.

## Стек

- Бекенд: Java 21, Spring Boot 4.1, Maven
- Фронтенд: React 19, Vite, TypeScript, Tailwind CSS 4, shadcn/ui
- Хранилище: in-memory (в памяти процесса; PostgreSQL запланирован отдельным шагом)
- Тесты: JUnit 5 (дымовой), Vitest (дымовой)
- Окружение: mise (версии, задачи, daemons)
- CI: GitHub Actions → `mise run check` на каждый push
- Коммиты: Conventional Commits

Архитектурные решения: [docs/decisions.md](docs/decisions.md). Правила проекта для агентов: [AGENTS.md](AGENTS.md).

## Релизы

Релизы готовит [release-please](https://github.com/googleapis/release-please) по Conventional Commits (`.github/workflows/release-please.yml`):

1. После push (слияния) в `main` workflow анализирует коммиты и создаёт или обновляет release-PR — «chore: release X.Y.Z» с автоописанием изменений (`feat:` → минор, `fix:`/`docs:` и прочие → патч).
2. Release-PR ревьюится и мержится обычным коммитом в `main`.
3. Мерж release-PR публикует GitHub Release и поднимает версию (`version.txt`).

Ручных тегов не ставим: версию и changelog ведёт release-please.

## Docker

Приложение упаковано в один образ (`Dockerfile` в корне): фронтенд собирается Vite и укладывается в Spring Boot как статика, всё отдаётся с одного порта. Порт берётся из переменной окружения `PORT` (дефолт 8080).

```bash
docker build -t calendar .
docker run --rm -p 9123:9123 -e PORT=9123 calendar
# → http://localhost:9123 (SPA) и /api/**, GET /api/health для проверки
```

## Деплой и MCP-конфигурация (Render)

Публикация — через **Render MCP** (запасной вариант — Railway MCP, тот же образ). Конфигурация агента лежит в `opencode.json` и **не содержит секретов**:

```json
"render": {
  "type": "remote",
  "url": "https://mcp.render.com/mcp",
  "oauth": false,
  "headers": { "Authorization": "Bearer {env:RENDER_API_KEY}" }
}
```

Порядок подключения:

1. Создайте API-ключ в Render Dashboard → Account Settings → API Keys (OAuth для этого MCP в OpenCode не поддерживается, используется ключ).
2. Скопируйте `.env.example` в `.env` и заполните `RENDER_API_KEY=...` (файл `.env` в `.gitignore`), либо экспортируйте переменную в окружение: `export RENDER_API_KEY=...`.
3. Перезапустите OpenCode — MCP-сервер подхватит ключ из окружения.
4. Попросите агента задеплоить: сервис создаётся из `Dockerfile` (runtime `docker`), стартует по `PORT`, автодеплой включён для `main`.

После создания сервиса агент проверяет деплой (`GET /api/health`) и прогоняет сквозной сценарий по публичной ссылке. Файлы с реальными ключами в git не попадают.

## Установка

<!-- Опишите установку: клонирование, зависимости, переменные окружения -->

```bash
git clone https://github.com/abai45/ai-for-developers-project-387.git
cd ai-for-developers-project-387
```

## Использование

Зависимости и проверки — через [mise](https://mise.jdx.dev/) (Java 21 и Node 22 подтягиваются автоматически):

```bash
mise run setup     # зависимости бекенда, фронтенда, TypeSpec и Playwright
mise run check     # все проверки: контракт, тесты, линт, типы, сборка, E2E (то же в CI)
```

Запуск приложения:

```bash
mise run backend    # http://localhost:8080 — API и SPA
mise run frontend   # http://localhost:5173 — dev-сервер с прокси на /api
```

Либо одним контейнером (см. [Docker](#docker)):

```bash
docker build -t calendar .
docker run --rm -p 9123:9123 -e PORT=9123 calendar
# → http://localhost:9123
```

Живой сервис: https://calendar-zvonkov.onrender.com

<!-- Добавьте примеры запуска и запись asciinema — именно это смотрит работодатель -->

---

<details>
<summary>Автоматические тесты Хекслета</summary>

Тесты запускаются на каждый коммит. За запуск отвечает файл `.github/workflows/hexlet-check.yml` — не удаляйте и не переименовывайте ни его, ни репозиторий.

</details>

## О Хекслете

[Хекслет](https://ru.hexlet.io/) — школа программирования: авторские программы обучения с практикой, поддержкой наставников и реальными проектами, которые остаются в резюме. Этот репозиторий — один из таких проектов.
