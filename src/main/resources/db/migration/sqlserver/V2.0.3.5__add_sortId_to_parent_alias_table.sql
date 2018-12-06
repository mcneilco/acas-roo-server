-- add integer column sort_id

ALTER TABLE [parent_alias] ADD [sort_id] [int] NULL
CONSTRAINT [defaultSortId] DEFAULT 0;
GO

UPDATE [parent_alias] SET [sort_id] = 0;
