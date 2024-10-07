#!/usr/bin/env bash

# Find the directory this script is located in
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
GUIDES="$DIR/../../guides/**/*.adoc"

echo "## Common"
for snippet in $(ls $DIR/../docs/common/snippets); do
  trimmed_snippet=${snippet:7}

  if ! grep -q "common:$trimmed_snippet\[" $GUIDES $(ls -d $DIR/../docs/common/snippets/*.adoc) $(ls -d $DIR/../docs/common/callouts/*.adoc) \
    && ! grep -q "common:${trimmed_snippet%?????}\[" $GUIDES $(ls -d $DIR/../docs/common/snippets/*.adoc) $(ls -d $DIR/../docs/common/callouts/*.adoc); then
    echo "Unused common snippet: src/docs/common/snippets/$snippet"
  fi
done

echo
echo "## Callouts"
for snippet in $(ls $DIR/../docs/common/callouts); do
  trimmed_snippet=${snippet:8}

  if ! grep -q "callout:$trimmed_snippet\[" $GUIDES $(ls -d $DIR/../docs/common/snippets/*.adoc) $(ls -d $DIR/../docs/common/callouts/*.adoc) \
    && ! grep -q "callout:${trimmed_snippet%?????}\[" $GUIDES $(ls -d $DIR/../docs/common/snippets/*.adoc) $(ls -d $DIR/../docs/common/callouts/*.adoc); then
    echo "Unused callout snippet: src/docs/common/callouts/$snippet"
  fi
done

echo
echo "## Images"
IMAGES=$({ cd $DIR/../docs/images && find . -type f -not -path "*/cards/*"; } | tail -n +2 | cut -c 3-)
for image in $IMAGES; do
  if ! grep -q "image::$image\[" $GUIDES $(ls -d $DIR/../docs/common/snippets/*.adoc) $(ls -d $DIR/../docs/common/callouts/*.adoc) \
    && ! grep -q "image:$image\[" $GUIDES $(ls -d $DIR/../docs/common/snippets/*.adoc) $(ls -d $DIR/../docs/common/callouts/*.adoc) \
    && ! grep -q "images/$image" $(find $DIR/../../buildSrc -type f) ; then
    echo "Unused image: src/docs/images/$image"
  fi
done
