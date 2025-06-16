CREATE INDEX idx_addon_group_id ON addon(addon_group_id); -- addon_group_id
CREATE INDEX idx_addon_name_trgm ON addon USING gin (name gin_trgm_ops); -- addon_name %like% 인덱싱