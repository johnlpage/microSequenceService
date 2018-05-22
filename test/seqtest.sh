for t in `seq 0 63`
do
rm -f curl$t.out
curl --silent -X POST 'http://localhost:4567/sequence?[0-2000]' >> curl$t.out &
done
time wait

echo "Checking for Duplicates"
if [[ $(cat curl*.out|sort| uniq -d) ]] ; then
 echo "ERROR: Duplicates Found" 
else
 echo "OK: No Duplicates found"
fi


