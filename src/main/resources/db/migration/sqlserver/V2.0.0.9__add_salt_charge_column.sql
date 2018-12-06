ALTER TABLE [salt] ADD [charge] [int] NULL
CONSTRAINT [defaultcharge] DEFAULT 0;
GO

UPDATE [salt] SET [charge] = 0;
