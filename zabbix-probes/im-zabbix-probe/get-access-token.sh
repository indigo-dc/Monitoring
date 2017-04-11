#!/bin/bash

IAM_USER='eoortiz'
IAM_PASSWORD='sotchturion'


if [[ -z ${IAM_USER} ]]; then
  read -p "Username: " IAM_USER
fi

#echo -ne "Password:"
#read -s IAM_PASSWORD
echo

result=$(curl -s -L \
  -d grant_type=password \
  -d client_id=d573b18e-7a59-4bed-bbbb-ebad2b5f8299 \
  -d client_secret=NxFfYvk19hiKuuLo50OO44nuwShRsV45UrvivWUwvF-4Szdadu2fuh7BwHLuttbZFeQYhYmEbbxdfSDrJajdNg \
  -d username=${IAM_USER} \
  -d password=${IAM_PASSWORD} \
  ${IAM_ENDPOINT:-https://iam-test.indigo-datacloud.eu/token})

if [[ $? != 0 ]]; then
  echo "Error!"
  echo $?
  echo $result
  exit 1
fi

echo $result

access_token=$(echo $result | jq -r .access_token)

#echo "export IAM_ACCESS_TOKEN=\"${access_token}\""
