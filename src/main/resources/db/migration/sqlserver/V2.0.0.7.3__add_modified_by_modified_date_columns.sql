ALTER TABLE [lot] ADD [modified_date] [datetime] NULL;
ALTER TABLE [lot] ADD [modified_by] [numeric](19, 0) NULL;
ALTER TABLE [parent] ADD [modified_date] [datetime] NULL;
ALTER TABLE [parent] ADD [modified_by] [numeric](19, 0) NULL;
ALTER TABLE [lot] WITH CHECK ADD CONSTRAINT [lot_modified_by_fk] FOREIGN KEY (modified_by) REFERENCES [scientist] ([id]);
ALTER TABLE [parent] WITH CHECK ADD CONSTRAINT [parent_modified_by_fk] FOREIGN KEY (modified_by) REFERENCES [scientist]([id]);
