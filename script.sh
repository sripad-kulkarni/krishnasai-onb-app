pg_dump -F p --no-acl --no-owner --quote-all-identifiers $DATABASE_URL > sampledb.dump
cat sampledb.dump