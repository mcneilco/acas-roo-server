# /bin/bash

# Find all the flyway migration files and sort them in order of version number (semantically)
# write them out as tab separated values with version\tfile

java_files=$( find src/main/java/com/labsynch/labseer/db/migration -name "V*.java" )

# Loop through the files and get the version from the beginning of the file name
# e.g. src/main/java/com/labsynch/labseer/db/migration/postgres/V1_0_6_6__copy_author_value_blob_values.java -> 1_0_6_6
all_versions=""
for java_file in $java_files
do
    file_name=$( basename $java_file )
    version=$( echo $file_name | sed -e 's/V\([0-9_]*\)__.*/\1/' | sed -e 's/_/./g' )
    # add java to the end of the version
    version="$version\t$java_file"
    all_versions="$all_versions $version"
done

# Get teh sql files in src/main/resources/db/migration/postgres
sql_files=$( find src/main/resources/db/migration -name "*.sql" )

# Loop through the files and get the version from the beginning of the file name
# e.g. src/main/resources/db/migration/postgres/V1_0_6_6__copy_author_value_blob_values.sql -> 1_0_6_6
for sql_file in $sql_files
do
    file_name=$( basename $sql_file )
    version=$( echo $file_name | sed -e 's/V\([0-9.]*\)__.*/\1/' )
    version="$version\t$sql_file"
    all_versions="$all_versions $version"
done


# Do a semantically sorted list of the versions
# Use printf to get the tabs to work
printf "version\tfile"
printf "$all_versions" | tr ' ' '\n' | sort -V

