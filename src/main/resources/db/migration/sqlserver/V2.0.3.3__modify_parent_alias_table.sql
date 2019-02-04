-- create indexes
-- recreate the kind tables

CREATE NONCLUSTERED INDEX [parent_alias_parent_idx] ON [parent_alias]
(
	[parent] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)

ALTER TABLE [parent_alias]  WITH CHECK ADD  CONSTRAINT [parent_alias_parent_fk] FOREIGN KEY(parent)
REFERENCES [parent] ([id]);

CREATE NONCLUSTERED INDEX [salt_form_alias_saltform_idx] ON [salt_form_alias]
(
	[salt_form] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)

ALTER TABLE [salt_form_alias]  WITH CHECK ADD  CONSTRAINT [salt_form_alias_saltform_fk] FOREIGN KEY(salt_form)
REFERENCES [salt_form] ([id]);

CREATE NONCLUSTERED INDEX [lot_alias_lot_idx] ON [lot_alias]
(
	[lot] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)

ALTER TABLE [lot_alias]  WITH CHECK ADD  CONSTRAINT [lot_alias_lot_fk] FOREIGN KEY(lot)
REFERENCES [lot] ([id]);

--

--

--
  
GO
sp_rename 'parent_alias_kind.parent_alias_type', 'ls_type', 'COLUMN'; 
GO
sp_rename 'salt_form_alias_kind.salt_form_alias_type', 'ls_type', 'COLUMN'; 
GO
sp_rename 'lot_alias_kind.lot_alias_type', 'ls_type', 'COLUMN'; 
	
--

--
  
--



