#!/usr/bin/env bash
set -euo pipefail

# DB 접속 정보: 환경변수에 설정하거나 기본값 지정
DB_HOST="${PGHOST:-localhost}"
DB_PORT="${PGPORT:-5432}"
DB_USER="${PGUSER:-postgres}"
DB_PASSWORD="${PGPASSWORD:-postgres}"
DB_NAME="${PGDATABASE:-postgres}"
# PGPASSWORD는 환경변수에 설정되어 있으면 psql 호출 시 자동 사용

# 스크립트 위치 기준으로 SQL_DIR 지정 (스크립트가 postgresql-init 폴더 안에 있을 때)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_DIR="$SCRIPT_DIR"

# 실행할 파일 목록 (순서가 중요하면 적절히 정렬)
FILES=(
  "addon_group.sql"
  "addon.sql"
  "mplan_detail.sql"
  "mplan.sql"
  "combined.sql"
  "member.sql"
)

# 디렉터리 확인
if [ ! -d "$SQL_DIR" ]; then
  echo "Error: SQL 디렉터리($SQL_DIR)가 존재하지 않습니다." >&2
  exit 1
fi

echo "=== PostgreSQL 초기화: SQL_DIR=$SQL_DIR ==="
for fname in "${FILES[@]}"; do
  fullpath="$SQL_DIR/$fname"
  if [ -f "$fullpath" ]; then
    echo "[`date +'%Y-%m-%d %H:%M:%S'`] Executing: $fname"
    PGPASSWORD="$DB_PASSWORD" \
      psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$fullpath"
    echo "[`date +'%Y-%m-%d %H:%M:%S'`] Finished: $fname"
  else
    echo "Warning: 파일이 없습니다: $fullpath (스킵)" >&2
  fi
done
echo "=== 모든 SQL 실행 완료 ==="