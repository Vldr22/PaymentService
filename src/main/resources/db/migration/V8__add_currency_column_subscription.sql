-- V8: Добавление валюты подписки
ALTER TABLE subscriptions
    ADD COLUMN currency VARCHAR(10) NOT NULL DEFAULT 'USD';
COMMENT ON COLUMN subscriptions.currency IS 'Валюта подписки; фиксируется при создании';