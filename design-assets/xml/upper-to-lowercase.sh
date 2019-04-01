for f in *; do
    test -f "$f" && echo mv "$f" "$( tr '[:upper:]' '[:lower:]' <<<"$f" )"
done
