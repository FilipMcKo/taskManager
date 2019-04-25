ALTER TABLE task
modify `name` varchar(255) NOT NULL,
modify `current_state` varchar(255) NOT NULL,
modify `is_not_running` bit(1) NOT NULL;