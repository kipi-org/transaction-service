create or replace function goal_and_limit_update_after_insert()
    returns trigger
    language plpgsql
as
$$
BEGIN
    update goals set "currentAmount" = "currentAmount" + NEW."amount" where "categoryId" = NEW."categoryId";
    update limits set "currentAmount" = "currentAmount" + NEW."amount" where "categoryId" = NEW."categoryId";
    return NEW;
END
$$;

create or replace function goal_and_limit_update_after_delete()
    returns trigger
    language plpgsql
as
$$
BEGIN
    update goals set "currentAmount" = "currentAmount" - OLD."amount" where "categoryId" = OLD."categoryId";
    update limits set "currentAmount" = "currentAmount" - OLD."amount" where "categoryId" = OLD."categoryId";
    return OLD;
END
$$;

create or replace function account_update_after_delete()
    returns trigger
    language plpgsql
as
$$
BEGIN
    update accounts set "balance" = "balance" - OLD."amount" where "id" = OLD."accountId";
    return OLD;
END
$$;

create or replace function account_update_after_insert()
    returns trigger
    language plpgsql
as
$$
BEGIN
    update accounts set "balance" = "balance" + NEW."amount" where "id" = NEW."accountId";
    return NEW;
END
$$;

create trigger goal_and_limit_recalculating_after_insert
    after insert
    on transactions
    FOR EACH ROW
    when (NEW.date >= make_date(cast(extract(YEAR from now()) as int),
                                cast(extract(MONTH from now()) as int), 1))
execute procedure goal_and_limit_update_after_insert();

create trigger goal_and_limit_recalculating_after_delete
    after delete
    on transactions
    FOR EACH ROW
execute procedure goal_and_limit_update_after_delete();

create trigger account_amount_recalculating_after_insert
    after insert
    on transactions
    FOR EACH ROW
execute procedure account_update_after_insert();

create trigger account_amount_recalculating_after_delete
    after delete
    on transactions
    FOR EACH ROW
execute procedure account_update_after_delete();

