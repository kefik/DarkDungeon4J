#rem mvn package
java -jar target/dd4j-adventure-0.0.1-SNAPSHOT-onejar.jar \
     -a data/dungeons/dungeon-example \
     -h data/hero-agents \
     -r results \
     -v \
     -p \
     -t 5  