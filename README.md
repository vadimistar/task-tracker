# Task Tracker

Планировщик задач, реализован на микросервисной архитектуре:
 * task-tracker-api
 * task-tracker-email-sender
 * task-tracker-scheduler
 * task-tracker-frontend

ТЗ проекта: https://zhukovsd.github.io/java-backend-learning-course/projects/task-tracker/

## Локальный запуск

1. Переименовать .env.example в .env
2. Заполнить переменные окружения для почтового клиента: MAIL_FROM_ADDRESS, MAIL_HOST, MAIL_PASSWORD, MAIL_PORT, MAIL_USERNAME. (‼️ Необходимо использовать клиент, который работает с протоколом SSL)
3. Запустить docker compose:
```bash
docker-compose -f docker-compose-local.yml up
```

Приложение будет доступно на http://localhost
