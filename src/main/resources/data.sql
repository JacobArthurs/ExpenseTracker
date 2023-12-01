INSERT INTO category (title, description)
VALUES
    ('Housing', 'Expenses related to housing.'),
    ('Transportation', 'Costs associated with transportation.'),
    ('Food', 'Expenditures on food.'),
    ('Utilities', 'Costs for utilities.'),
    ('Insurance', 'Expenditures for various types of insurance coverage.'),
    ('Medical & Healthcare', 'Expenses related to medical and healthcare services.'),
    ('Saving, Investing, & Debt Payments', 'Allocations for saving, investing, and debt payments.'),
    ('Personal Spending', 'Personal discretionary spending.'),
    ('Recreation & Entertainment', 'Costs associated with recreation and entertainment.'),
    ('Miscellaneous', 'Miscellaneous expenses.');

INSERT INTO expected_category_distribution (category_id, minimum_distribution, maximum_distribution)
VALUES
    (1, 25, 35),  -- Housing
    (2, 10, 15),  -- Transportation
    (3, 10, 15),  -- Food
    (4, 5, 10),   -- Utilities
    (5, 10, 25),  -- Insurance
    (6, 5, 10),   -- Medical & Healthcare
    (7, 10, 20),  -- Saving, Investing, & Debt Payments
    (8, 5, 10),   -- Personal Spending
    (9, 5, 10),   -- Recreation & Entertainment
    (10, 5, 10);  -- Miscellaneous

--INSERT INTO expense (category_id, title, description, created_date, last_updated_date)
--VALUES
--    (1, 'test', 'test', '2023-11-30 13:56:02.845258', '2023-11-30 13:56:02.845258')