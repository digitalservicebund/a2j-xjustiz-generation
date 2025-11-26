mkdir -p tmp
curl -L "https://www.xrepository.de/api/version_standard/urn%3Axoev-de%3Ablk-ag-it-standards%3Astandard%3Axjustiz_3.6.2/genutzteAktuelleCodelisten" -o tmp/codes.zip

mkdir -p app/src/main/resources/codes
bsdtar -xf tmp/codes.zip -C app/src/main/resources/codes
rm tmp/codes.zip
find app/src/main/resources/codes -type f ! -name 'GDS*' ! -name 'KLAVER*' -delete
