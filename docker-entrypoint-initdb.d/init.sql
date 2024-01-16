-- init.sql
-- To be run only when a new docker volume 'postgres-data' is created.
-- Tables must be created in this script because they will not be created by jpa until runtime.

--Create tables
CREATE TABLE expense_tracker_user (
    id serial PRIMARY KEY,
    username VARCHAR ( 50 ) NOT NULL,
    password VARCHAR ( 200 ) NOT NULL,
    name VARCHAR ( 200 ) NOT NULL,
    email VARCHAR ( 200 ) NOT NULL,
    role VARCHAR ( 50 ) NOT NULL DEFAULT 'DEFAULT',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE category (
	id serial PRIMARY KEY,
	user_id INT NOT NULL,
	title VARCHAR ( 50 ) NOT NULL,
	description VARCHAR ( 200 ),
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES expense_tracker_user (id)
);

CREATE TABLE expected_category_distribution (
	id serial PRIMARY KEY,
	user_id INT NOT NULL,
	category_id INT NOT NULL,
	distribution int NOT NULL,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (category_id)
    	REFERENCES category (id),
    FOREIGN KEY (user_id)
        REFERENCES expense_tracker_user (id)
);

CREATE TABLE expense (
	id serial PRIMARY KEY,
	user_id INT NOT NULL,
	category_id INT NOT NULL,
	title VARCHAR ( 50 ) NOT NULL,
	description VARCHAR ( 200 ),
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (category_id)
    	REFERENCES category (id),
    FOREIGN KEY (user_id)
        REFERENCES expense_tracker_user (id)
);

--Insert default user, category, and expected_category_distribution data
INSERT INTO expense_tracker_user (username, password, name, email, role, created_date, last_updated_date)
VALUES
    ('default', '$2a$10$uZDxmarCD7zRvtY/L/gGIOGGlgZ4.4bKLVGnXSxb7u0Kjs454hps6', 'Default User', 'Default@JacobArthurs.com', 'DEFAULT', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('admin', '$2a$10$uZDxmarCD7zRvtY/L/gGIOGGlgZ4.4bKLVGnXSxb7u0Kjs454hps6', 'Admin User', 'Admin@JacobArthurs.com', 'ADMIN', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258');

INSERT INTO category (user_id, title, description, created_date, last_updated_date)
VALUES
    (1, 'Housing', 'Expenses related to housing.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Transportation', 'Costs associated with transportation.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Food', 'Expenditures on food.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Utilities', 'Costs for utilities.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Insurance', 'Expenditures for various types of insurance coverage.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Medical & Healthcare', 'Expenses related to medical and healthcare services.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Saving, Investing, & Debt Payments', 'Allocations for saving, investing, and debt payments.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Personal Spending', 'Personal discretionary spending.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Recreation & Entertainment', 'Costs associated with recreation and entertainment.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    (1, 'Miscellaneous', 'Miscellaneous expenses.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258');

INSERT INTO expected_category_distribution (user_id, category_id, distribution, created_date, last_updated_date)
VALUES
    (1, 1, 25, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Housing
    (1, 2, 15, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Transportation
    (1, 3, 15, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Food
    (1, 4, 10, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Utilities
    (1, 5, 10, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Insurance
    (1, 6, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Medical & Healthcare
    (1, 7, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Saving, Investing, & Debt Payments
    (1, 8, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Personal Spending
    (1, 9, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Recreation & Entertainment
    (1, 10, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258');  -- Miscellaneous

-- Function to generate dummy expense data based on the expected category distribution rates with variations
DO $$
DECLARE
    distribution_record RECORD;
    user_id INT;
    category_id INT;
    min_distribution INT;
    max_distribution INT;
    random_percentage INT;
    total_expenses INT := 1000;
    num_expenses INT;
    i INT;
    expense_date TIMESTAMP;
    title_prefix VARCHAR(255);
    description_prefix VARCHAR(255);
BEGIN
    BEGIN
        FOR distribution_record IN (SELECT * FROM expected_category_distribution) LOOP
            -- Extract values from the distribution_record
            category_id := distribution_record.category_id;
            min_distribution := distribution_record.distribution - 5;
            max_distribution := distribution_record.distribution + 5;

            -- Generate a random percentage within the specified distribution
            random_percentage := floor(random() * (max_distribution - min_distribution + 1) + min_distribution);

            -- Calculate the number of expenses based on the percentage
            num_expenses := CEIL((random_percentage / 100.0) * total_expenses);

            -- Set prefix for title and description based on category
            title_prefix := CASE
                WHEN category_id = 1 THEN 'Housing Expense'
                WHEN category_id = 2 THEN 'Transportation Cost'
                WHEN category_id = 3 THEN 'Food Expense'
                WHEN category_id = 4 THEN 'Utilities Expense'
                WHEN category_id = 5 THEN 'Insurance Expense'
                WHEN category_id = 6 THEN 'Medical & Healthcare Expense'
                WHEN category_id = 7 THEN 'Saving, Investing, & Debt Payments Expense'
                WHEN category_id = 8 THEN 'Personal Spending Expense'
                WHEN category_id = 9 THEN 'Recreation & Entertainment Expense'
                WHEN category_id = 10 THEN 'Miscellaneous Expense'
                ELSE 'Generic Expense'
            END;

            description_prefix := 'Details for ' || title_prefix;

            -- Insert dummy expense data into the expense table
            FOR i IN 1..num_expenses LOOP
                -- Generate a random date between current date and 6 months ago
                expense_date := CURRENT_DATE - (floor(random() * 180) || ' days')::INTERVAL;

                -- Insert dummy expense data
                INSERT INTO expense (user_id, category_id, title, description, created_date, last_updated_date)
                VALUES (user_id, category_id, title_prefix || ' ' || i, description_prefix || ' ' || i, expense_date, expense_date);
            END LOOP;
        END LOOP;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error: %', SQLERRM;
    END;

    COMMIT;
END
$$;