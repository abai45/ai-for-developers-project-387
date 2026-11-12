# Changelog

## [1.1.0](https://github.com/abai45/ai-for-developers-project-387/compare/v1.0.0...v1.1.0) (2026-11-12)


### Features

* ввод темы, экран «Вы записаны» и карточка владельца ([#15](https://github.com/abai45/ai-for-developers-project-387/issues/15)) ([de03d34](https://github.com/abai45/ai-for-developers-project-387/commit/de03d344688f7d4de58b2846b070becc1c189c9f))
* тема разговора в контракте и бэкенде ([#14](https://github.com/abai45/ai-for-developers-project-387/issues/14)) ([4b91ada](https://github.com/abai45/ai-for-developers-project-387/commit/4b91adaa62c6de2ae197b48a6139668da788a7e1))


### Bug Fixes

* publish-job передаёт --repo gh (нет checkout в этой задаче) ([e59c0c2](https://github.com/abai45/ai-for-developers-project-387/commit/e59c0c244db30745686d52f10140101bac517a57))
* V1-синтаксис permission — раннер ставит opencode v1.19.5 ([58ea594](https://github.com/abai45/ai-for-developers-project-387/commit/58ea594f88455b0d43d29b0e908d29538996a847))
* права issues:write и allow event-каталога — триаж не зависает ([1be54cd](https://github.com/abai45/ai-for-developers-project-387/commit/1be54cda1e7b12e81a7814f185a7510f90fc6c21))
* правки второго раунда авторевью — доверенные авторы в счёте, история по created_at, статус сбоя ревью ([e810e36](https://github.com/abai45/ai-for-developers-project-387/commit/e810e364d90b55f89a06afce68511d5e6b741e07))
* синтаксис permission V1 для external_directory — раннер ставит opencode v1.19 ([4fcbcea](https://github.com/abai45/ai-for-developers-project-387/commit/4fcbcea2351fa1d439c10cf432fa07bdd9e3b99b))
* тело запроса бронирования несёт обрезанную тему ([#5](https://github.com/abai45/ai-for-developers-project-387/issues/5)) ([cefcb75](https://github.com/abai45/ai-for-developers-project-387/commit/cefcb757371805229cfcc21d7c9b240ddb62e3b7))
* триаж не зависает — права issues:write + allow event-каталога ([cf16646](https://github.com/abai45/ai-for-developers-project-387/commit/cf166469e7adfc21facde072dafb65eeef195a70))
* формат spotless MeetingsApiTest ([#14](https://github.com/abai45/ai-for-developers-project-387/issues/14)) ([0019dea](https://github.com/abai45/ai-for-developers-project-387/commit/0019dea582dae22daac298826f40c27b3352de4a))

## 1.0.0 (2026-11-11)


### Features

* add landing and choice screens ([511d6ce](https://github.com/abai45/ai-for-developers-project-387/commit/511d6cec185ad94a9eb03f76c1dad30cb8b23a96))
* add React frontend skeleton with shadcn/ui ([fdbf40d](https://github.com/abai45/ai-for-developers-project-387/commit/fdbf40d7d9366e6ad12749eaf2b42d05090e830f))
* add Spring Boot backend skeleton with smoke test ([0fbc92e](https://github.com/abai45/ai-for-developers-project-387/commit/0fbc92e5f5ac37971dd58dfd0ae95d9117d24a28))
* Docker + подготовка деплоя через Render MCP ([2fc18c1](https://github.com/abai45/ai-for-developers-project-387/commit/2fc18c1e61df345745bb55ec33ce2529029c37ab))
* Docker-образ — статика фронтенда в джаре, запуск по PORT ([#19](https://github.com/abai45/ai-for-developers-project-387/issues/19)) ([3926c02](https://github.com/abai45/ai-for-developers-project-387/commit/3926c02e9ba7c112d4d74dfd28ae7cfe8d668754))
* GET /api/meetings — только предстоящие, по возрастанию времени ([#13](https://github.com/abai45/ai-for-developers-project-387/issues/13)) ([77383db](https://github.com/abai45/ai-for-developers-project-387/commit/77383db3ea93a915d7d876d8a036dd5fa66dada7))
* POST /api/event-types с серверной валидацией полей ([#8](https://github.com/abai45/ai-for-developers-project-387/issues/8)) ([4807652](https://github.com/abai45/ai-for-developers-project-387/commit/480765234d0a3613367d52d3d097185e581e7004))
* POST /api/meetings с атомарной проверкой пересечений ([#11](https://github.com/abai45/ai-for-developers-project-387/issues/11)) ([5cad644](https://github.com/abai45/ai-for-developers-project-387/commit/5cad6443281b8c60baa046f4103c4c340c6087d1))
* restrict owner section and simplify guest flow ([7c614ea](https://github.com/abai45/ai-for-developers-project-387/commit/7c614ea8a1fe036f2c4a41ba44a6c02309071ed1))
* TypeSpec-контракт и генерация OpenAPI с дрейф-чеком ([#7](https://github.com/abai45/ai-for-developers-project-387/issues/7)) ([29ba4b1](https://github.com/abai45/ai-for-developers-project-387/commit/29ba4b1870c72a9b9933316c5ca3b144b5e0a2e9))
* занятость слотов по существующим встречам, включая другие типы ([#12](https://github.com/abai45/ai-for-developers-project-387/issues/12)) ([8c1d213](https://github.com/abai45/ai-for-developers-project-387/commit/8c1d213bae2d22cde3a270550af8fd8a03d81b33))
* Календарь звонков — реализация спеки [#6](https://github.com/abai45/ai-for-developers-project-387/issues/6) ([aaa2322](https://github.com/abai45/ai-for-developers-project-387/commit/aaa2322c01b0404171c774410a43d66887c41d17))
* Календарь звонков — реализация спеки [#6](https://github.com/abai45/ai-for-developers-project-387/issues/6) ([#15](https://github.com/abai45/ai-for-developers-project-387/issues/15)) ([aaa2322](https://github.com/abai45/ai-for-developers-project-387/commit/aaa2322c01b0404171c774410a43d66887c41d17))
* календарь слотов — шаг «Календарь» мастера записи ([#10](https://github.com/abai45/ai-for-developers-project-387/issues/10)) ([b1261d0](https://github.com/abai45/ai-for-developers-project-387/commit/b1261d04ebec4b2229ce32340556e4df87a25587))
* контракт записи гостя: POST /api/meetings, 409 и MeetingView ([#11](https://github.com/abai45/ai-for-developers-project-387/issues/11)) ([d2c97e1](https://github.com/abai45/ai-for-developers-project-387/commit/d2c97e115acff94be91711186f4f90206ca5abf9))
* контракт и серверная выдача предстоящих встреч ([#13](https://github.com/abai45/ai-for-developers-project-387/issues/13)) ([027c7e5](https://github.com/abai45/ai-for-developers-project-387/commit/027c7e5106316d0cfe721f8ede4f789ff9457477))
* контракт создания типа встречи с 400 problem+json ([#8](https://github.com/abai45/ai-for-developers-project-387/issues/8)) ([1d28865](https://github.com/abai45/ai-for-developers-project-387/commit/1d2886588b0aa8abe484eb374b683531141151f7))
* секция «Предстоящие встречи» на странице владельца ([#13](https://github.com/abai45/ai-for-developers-project-387/issues/13)) ([c726c80](https://github.com/abai45/ai-for-developers-project-387/commit/c726c8054912eabc136eb3dd9b22d5242fe38252))
* секция «Типы встреч» на странице владельца ([#7](https://github.com/abai45/ai-for-developers-project-387/issues/7)) ([ce57371](https://github.com/abai45/ai-for-developers-project-387/commit/ce573712700e7cceaeda7fafee564c55b5f3b314))
* серверный список типов встреч по контракту ([#7](https://github.com/abai45/ai-for-developers-project-387/issues/7)) ([a12828b](https://github.com/abai45/ai-for-developers-project-387/commit/a12828b7c4cc1fd01c47fd3cf318df96963afaa5))
* удобная форма владельца — дефолты 30/15, чипы выбора и превью слотов ([#16](https://github.com/abai45/ai-for-developers-project-387/issues/16)) ([767485b](https://github.com/abai45/ai-for-developers-project-387/commit/767485be9ac6cafc285d4da1fe767ce5a3a0fecf))
* форма создания типа встречи у владельца с тостом и refresh списка ([#8](https://github.com/abai45/ai-for-developers-project-387/issues/8)) ([37a13fb](https://github.com/abai45/ai-for-developers-project-387/commit/37a13fb6b1590c3a347268416bc6685df7cd1714))
* шаги «Имя» и «Подтверждение», экран «Вы записаны» и ветка 409 ([#11](https://github.com/abai45/ai-for-developers-project-387/issues/11)) ([c3d5773](https://github.com/abai45/ai-for-developers-project-387/commit/c3d5773ac1d533e819a68492481adab19d43f7c0))


### Bug Fixes

* серверная граница записи — только слот из окна, 400 вместо 500 ([#11](https://github.com/abai45/ai-for-developers-project-387/issues/11)) ([e229e9f](https://github.com/abai45/ai-for-developers-project-387/commit/e229e9fa574f094ce3a5fa9d1d877ed62cb28830))
* скелетон шага «Календарь» пока окно не получено с сервера ([#14](https://github.com/abai45/ai-for-developers-project-387/issues/14)) ([3ad551d](https://github.com/abai45/ai-for-developers-project-387/commit/3ad551d77b7c0d965e3e36ea303fed5d121b0872))

## [1.1.0](https://github.com/abai45/ai-for-developers-project-386/compare/v1.0.0...v1.1.0) (2026-11-01)


### Features

* Docker + подготовка деплоя через Render MCP ([2fc18c1](https://github.com/abai45/ai-for-developers-project-386/commit/2fc18c1e61df345745bb55ec33ce2529029c37ab))
* Docker-образ — статика фронтенда в джаре, запуск по PORT ([#19](https://github.com/abai45/ai-for-developers-project-386/issues/19)) ([3926c02](https://github.com/abai45/ai-for-developers-project-386/commit/3926c02e9ba7c112d4d74dfd28ae7cfe8d668754))

## 1.0.0 (2026-11-01)


### Features

* add landing and choice screens ([511d6ce](https://github.com/abai45/ai-for-developers-project-386/commit/511d6cec185ad94a9eb03f76c1dad30cb8b23a96))
* add React frontend skeleton with shadcn/ui ([fdbf40d](https://github.com/abai45/ai-for-developers-project-386/commit/fdbf40d7d9366e6ad12749eaf2b42d05090e830f))
* add Spring Boot backend skeleton with smoke test ([0fbc92e](https://github.com/abai45/ai-for-developers-project-386/commit/0fbc92e5f5ac37971dd58dfd0ae95d9117d24a28))
* GET /api/meetings — только предстоящие, по возрастанию времени ([#13](https://github.com/abai45/ai-for-developers-project-386/issues/13)) ([77383db](https://github.com/abai45/ai-for-developers-project-386/commit/77383db3ea93a915d7d876d8a036dd5fa66dada7))
* POST /api/event-types с серверной валидацией полей ([#8](https://github.com/abai45/ai-for-developers-project-386/issues/8)) ([4807652](https://github.com/abai45/ai-for-developers-project-386/commit/480765234d0a3613367d52d3d097185e581e7004))
* POST /api/meetings с атомарной проверкой пересечений ([#11](https://github.com/abai45/ai-for-developers-project-386/issues/11)) ([5cad644](https://github.com/abai45/ai-for-developers-project-386/commit/5cad6443281b8c60baa046f4103c4c340c6087d1))
* restrict owner section and simplify guest flow ([7c614ea](https://github.com/abai45/ai-for-developers-project-386/commit/7c614ea8a1fe036f2c4a41ba44a6c02309071ed1))
* TypeSpec-контракт и генерация OpenAPI с дрейф-чеком ([#7](https://github.com/abai45/ai-for-developers-project-386/issues/7)) ([29ba4b1](https://github.com/abai45/ai-for-developers-project-386/commit/29ba4b1870c72a9b9933316c5ca3b144b5e0a2e9))
* занятость слотов по существующим встречам, включая другие типы ([#12](https://github.com/abai45/ai-for-developers-project-386/issues/12)) ([8c1d213](https://github.com/abai45/ai-for-developers-project-386/commit/8c1d213bae2d22cde3a270550af8fd8a03d81b33))
* Календарь звонков — реализация спеки [#6](https://github.com/abai45/ai-for-developers-project-386/issues/6) ([aaa2322](https://github.com/abai45/ai-for-developers-project-386/commit/aaa2322c01b0404171c774410a43d66887c41d17))
* Календарь звонков — реализация спеки [#6](https://github.com/abai45/ai-for-developers-project-386/issues/6) ([#15](https://github.com/abai45/ai-for-developers-project-386/issues/15)) ([aaa2322](https://github.com/abai45/ai-for-developers-project-386/commit/aaa2322c01b0404171c774410a43d66887c41d17))
* календарь слотов — шаг «Календарь» мастера записи ([#10](https://github.com/abai45/ai-for-developers-project-386/issues/10)) ([b1261d0](https://github.com/abai45/ai-for-developers-project-386/commit/b1261d04ebec4b2229ce32340556e4df87a25587))
* контракт записи гостя: POST /api/meetings, 409 и MeetingView ([#11](https://github.com/abai45/ai-for-developers-project-386/issues/11)) ([d2c97e1](https://github.com/abai45/ai-for-developers-project-386/commit/d2c97e115acff94be91711186f4f90206ca5abf9))
* контракт и серверная выдача предстоящих встреч ([#13](https://github.com/abai45/ai-for-developers-project-386/issues/13)) ([027c7e5](https://github.com/abai45/ai-for-developers-project-386/commit/027c7e5106316d0cfe721f8ede4f789ff9457477))
* контракт создания типа встречи с 400 problem+json ([#8](https://github.com/abai45/ai-for-developers-project-386/issues/8)) ([1d28865](https://github.com/abai45/ai-for-developers-project-386/commit/1d2886588b0aa8abe484eb374b683531141151f7))
* секция «Предстоящие встречи» на странице владельца ([#13](https://github.com/abai45/ai-for-developers-project-386/issues/13)) ([c726c80](https://github.com/abai45/ai-for-developers-project-386/commit/c726c8054912eabc136eb3dd9b22d5242fe38252))
* секция «Типы встреч» на странице владельца ([#7](https://github.com/abai45/ai-for-developers-project-386/issues/7)) ([ce57371](https://github.com/abai45/ai-for-developers-project-386/commit/ce573712700e7cceaeda7fafee564c55b5f3b314))
* серверный список типов встреч по контракту ([#7](https://github.com/abai45/ai-for-developers-project-386/issues/7)) ([a12828b](https://github.com/abai45/ai-for-developers-project-386/commit/a12828b7c4cc1fd01c47fd3cf318df96963afaa5))
* удобная форма владельца — дефолты 30/15, чипы выбора и превью слотов ([#16](https://github.com/abai45/ai-for-developers-project-386/issues/16)) ([767485b](https://github.com/abai45/ai-for-developers-project-386/commit/767485be9ac6cafc285d4da1fe767ce5a3a0fecf))
* форма создания типа встречи у владельца с тостом и refresh списка ([#8](https://github.com/abai45/ai-for-developers-project-386/issues/8)) ([37a13fb](https://github.com/abai45/ai-for-developers-project-386/commit/37a13fb6b1590c3a347268416bc6685df7cd1714))
* шаги «Имя» и «Подтверждение», экран «Вы записаны» и ветка 409 ([#11](https://github.com/abai45/ai-for-developers-project-386/issues/11)) ([c3d5773](https://github.com/abai45/ai-for-developers-project-386/commit/c3d5773ac1d533e819a68492481adab19d43f7c0))


### Bug Fixes

* серверная граница записи — только слот из окна, 400 вместо 500 ([#11](https://github.com/abai45/ai-for-developers-project-386/issues/11)) ([e229e9f](https://github.com/abai45/ai-for-developers-project-386/commit/e229e9fa574f094ce3a5fa9d1d877ed62cb28830))
* скелетон шага «Календарь» пока окно не получено с сервера ([#14](https://github.com/abai45/ai-for-developers-project-386/issues/14)) ([3ad551d](https://github.com/abai45/ai-for-developers-project-386/commit/3ad551d77b7c0d965e3e36ea303fed5d121b0872))
