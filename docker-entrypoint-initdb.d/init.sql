-- init.sql
-- To be run only when a new docker volume 'postgres-data' is created.
-- Tables must be created in this script because they will not be created by jpa until runtime.

--Create tables
CREATE TABLE category (
	id serial PRIMARY KEY,
	title VARCHAR ( 50 ) NOT NULL,
	description VARCHAR ( 200 ),
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE expected_category_distribution (
	id serial PRIMARY KEY,
	category_id INT NOT NULL,
	distribution int NOT NULL,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (category_id)
    	REFERENCES category (id)
);

CREATE TABLE expense (
	id serial PRIMARY KEY,
	category_id INT NOT NULL,
	title VARCHAR ( 50 ) NOT NULL,
	description VARCHAR ( 200 ),
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (category_id)
    	REFERENCES category (id)
);

--Insert default category and expected_category_distribution data
INSERT INTO category (title, description, created_date, last_updated_date)
VALUES
    ('Housing', 'Expenses related to housing.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Transportation', 'Costs associated with transportation.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Food', 'Expenditures on food.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Utilities', 'Costs for utilities.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Insurance', 'Expenditures for various types of insurance coverage.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Medical & Healthcare', 'Expenses related to medical and healthcare services.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Saving, Investing, & Debt Payments', 'Allocations for saving, investing, and debt payments.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Personal Spending', 'Personal discretionary spending.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Recreation & Entertainment', 'Costs associated with recreation and entertainment.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),
    ('Miscellaneous', 'Miscellaneous expenses.', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258');

INSERT INTO expected_category_distribution (category_id, distribution, created_date, last_updated_date)
VALUES
    (1, 25, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Housing
    (2, 15, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Transportation
    (3, 15, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Food
    (4, 10, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Utilities
    (5, 10, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),  -- Insurance
    (6, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Medical & Healthcare
    (7, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Saving, Investing, & Debt Payments
    (8, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Personal Spending
    (9, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258'),   -- Recreation & Entertainment
    (10, 5, '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258');  -- Miscellaneous

-- Function to generate dummy expense data based on the expected category distribution rates with variations
DO $$
DECLARE
    distribution_record RECORD;
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
    -- Start a transaction
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
                INSERT INTO expense (category_id, title, description, created_date, last_updated_date)
                VALUES (category_id, title_prefix || ' ' || i, description_prefix || ' ' || i, expense_date, expense_date);
            END LOOP;
        END LOOP;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error: %', SQLERRM;
    END;

    COMMIT;
END
$$;