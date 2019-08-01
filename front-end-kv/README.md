# Node etc

Use a somewhat latest node

    nvm use 12.3

# Starting frontend

    ./gradlew -t :front-end-kv:stop :front-end-kv:build :front-end-kv:run -x test
