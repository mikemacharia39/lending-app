Use the tool https://dbdiagram.io/d/ to visualize the schema

````sql

Table product {
id bigint [pk]
code varchar(30) [unique]
name text
description text
loan_tenure enum(FIXED,FLEXIBLE)
min_loan_amount bigdecimal(19,2)
max_loan_amount bigdecimal(19,2)
min_loan_term_duration int
max_loan_term_duration int
repayment_frequency enum(ONCE,DAILY,MONTHLY)
grace_period_before_loan_due_date_in_days int
grace_period_after_loan_due_date_in_days int
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table product_fee {
id bigint [pk]
code varchar(30) [unique]
product_id bigint
name text
fee_type enum(LATE_FEE, SERVICE_FEE, DAILY_FEE)
value_type enum(FIXED_AMOUNT,PERCENTAGE)
value bigdecimal(19,2)
applied_at_origination boolean
daily_accrual boolean
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table customer {
id bigint [pk]
code varchar(30) [unique]
first_name varchar(50)
last_name varchar(50)
email varchar(100)
phone_number text
notification_channel enum(SMS, EMAIL, PUSH_NOTIFICATION)
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table customer_address {
id bigint [pk]
code varchar(30) [unique]
customer_id bigint
addressLine varchar(100)
town varchar(100)
state varchar(50)
country varchar(50)
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table loan {
id bigint [pk]
code varchar(30) [unique]
customer_id bigint
product_id bigint
requested_amount bigdecimal(19,2)
disbursed_amount bigdecimal(19,2)
loan_state enum(OPEN,CLOSED)
loan_period int
loan_term enum(DAY,WEEK,MONTH,YEAR)
due_date datetime
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table loan_fee {
id bigint [pk]
loan_id bigint
amount bigdecimal(19,2)
applied_date datetime
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table loan_installment {
id bigint [pk]
code varchar(30) [unique]
loan_id bigint
amount bigdecimal(19,2)
due_date datetime
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

Table loan_repayment {
id bigint [pk]
code varchar(30) [unique]
loan_id bigint
amount bigdecimal(19,2)
paymentChannel varchar(30)
paymentReference varchar(30)
payment_date datetime
date_created datetime [default: `GETUTCDATE()`]
date_modified datetime [default: `GETUTCDATE()`]
}

// > many-to-one; < one-to-many; - one-to-one
Ref: product.id < product_fee.product_id
Ref: customer.id < customer_address.customer_id
Ref: product.id < loan.product_id
Ref: customer.id < loan.customer_id
Ref: loan.id < loan_fee.loan_id
Ref: loan.id < loan_installment.loan_id
Ref: loan.id < loan_repayment.loan_id

````
