-- V9: Удаление устаревшей таблицы sms_codes и обновление комментария subscription_status

DROP TABLE IF EXISTS sms_codes;

COMMENT ON COLUMN subscriptions.subscription_status IS
    'Статус подписки: ACTIVE — активна; PAST_DUE — платёж просрочен, идут retry;
     PROCESSING — PaymentIntent создан, ожидаем webhook;
     SUSPENDED — retry исчерпаны; CANCELLED — отменена';