#!/usr/bin/env bash
set -euo pipefail
BASE_URL="http://localhost:8080"
UI_USERS="${BASE_URL}/ui/admin/users"

echo "Checking unauthenticated access to $UI_USERS"
HTTP_UNAUTH=$(curl -s -o /dev/null -w "%{http_code}" -I "$UI_USERS" || true)
echo "Unauthenticated status: $HTTP_UNAUTH"

echo "Checking authenticated access to $UI_USERS (admin:admin123)"
HTTP_AUTH=$(curl -s -o /dev/null -w "%{http_code}" -u admin:admin123 -I "$UI_USERS" || true)
echo "Authenticated status: $HTTP_AUTH"

if [[ "$HTTP_AUTH" == "200" ]]; then
  echo "OK: Authenticated UI returned 200"
else
  echo "FAIL: Authenticated UI returned $HTTP_AUTH"
  exit 2
fi

if [[ "$HTTP_UNAUTH" == "401" || "$HTTP_UNAUTH" == "302" ]]; then
  echo "OK: Unauthenticated access blocked (status $HTTP_UNAUTH)"
else
  echo "WARN: Unauthenticated access returned $HTTP_UNAUTH"
fi
