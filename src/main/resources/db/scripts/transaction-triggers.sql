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

