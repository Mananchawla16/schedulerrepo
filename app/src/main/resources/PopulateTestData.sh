task(){
   curl --location --request POST 'https://notification-prf-scheduler.growthppdusw2.iks2.a.intuit.com/v1/jobs/schedule-oinp-job' \
   --header 'Content-Type: application/json' \
   --header 'Authorization: Intuit_IAM_Authentication intuit_appid="Intuit.intcollabs.notification.testscheduler",  intuit_app_secret="preprdBCVLELCYQ5zXDcJ9DdXz2g9HO1zEhu1nFu", intuit_userid="4620816365747796540",intuit_token="V1-60-X3hids7rgi9ud5fralvwkr",intuit_token_type=IAM-Ticket' \
   --header 'intuit_tid: 31c4a538-c05a-4134-a5bc-64428356a516-testN14' \
   --data-raw '{
       "scheduleId": "demo-tpm2",
       "triggerAt": "2022-06-16T09:20:00+01:00",
       "graceTime": "",
       "triggerData": "{\"authId\":\"ghfhgj\",\"sourceObjectId\":\"ghhg\",\"sourceServiceId\":\"fhg\",\"creationDate\":\"gfhj\"}"
   }'
   printf "\n"
}

N=5

(for i in {1..500}; do
  ((i=i%N)); ((i++==0)) && wait
  task &
 done
 )


#(for i in {1..10}; do
#     curl --location --request POST 'https://notification-prf-scheduler.growthppdusw2.iks2.a.intuit.com/v1/jobs/schedule-oinp-job' \
#     --header 'Content-Type: application/json' \
#     --header 'Authorization: Intuit_IAM_Authentication intuit_appid="Intuit.intcollabs.notification.testscheduler",  intuit_app_secret="preprdBCVLELCYQ5zXDcJ9DdXz2g9HO1zEhu1nFu", intuit_userid="4620816365747796540",intuit_token="V1-58-X3eh58wimf91a3rb8jlwi8",intuit_token_type=IAM-Ticket' \
#     --header 'intuit_tid: 31c4a538-c05a-4134-a5bc-64428356a516-testN10' \
#     --data-raw '{
#         "scheduleId": "demo-parallel",
#         "triggerAt": "2022-06-16T00:05:00+01:00",
#         "graceTime": "",
#         "triggerData": "{\"authId\":\"ghfhgj\",\"sourceObjectId\":\"ghhg\",\"sourceServiceId\":\"fhg\",\"creationDate\":\"gfhj\"}"
#     }'
# done
# )
