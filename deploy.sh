APP_NAME="fundebt"
AUTHOR="carlos-molero"
SSH_USERNAME=$SSH_USERNAME
SSH_PASSWORD=$SSH_PASSWORD
INSTANCE_IP=$INSTANCE_IP
REPO="git@github.com:carlos-molero/fundebt.git"

sshpass -p "$SSH_PASSWORD" ssh -o StrictHostKeyChecking=no ${SSH_USERNAME}@${INSTANCE_IP} -y "
sudo rm -rf ./$APP_NAME
git clone -b master $REPO ./$APP_NAME

docker-compose -f ./$APP_NAME/docker-compose.yml down
docker container prune -f
docker image prune -f

export SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
export SPRING_DATASOURCE_PASSWORD='$SPRING_DATASOURCE_PASSWORD'
export USER1_PASSWORD=$USER1_PASSWORD
export USER2_PASSWORD=$USER2_PASSWORD
export USER1_EMAIL=$USER1_EMAIL
export USER2_EMAIL=$USER2_EMAIL
export ACCESS_TOKEN_SECRET='$ACCESS_TOKEN_SECRET'
export REACT_APP_REDUX_PERSIST_KEY='$REACT_APP_REDUX_PERSIST_KEY'
export REACT_APP_CURRENCY='$REACT_APP_CURRENCY'
export ALLOWED_ORIGINS=$ALLOWED_ORIGINS
export ACCESS_TOKEN_EXPIRATION_DAYS=$ACCESS_TOKEN_EXPIRATION_DAYS

docker-compose -f ./$APP_NAME/docker-compose.yml config | docker-compose -f ./$APP_NAME/docker-compose.yml build
docker-compose -f ./$APP_NAME/docker-compose.yml config | docker-compose -f ./$APP_NAME/docker-compose.yml up --force-recreate --detach
"