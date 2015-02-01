in=../input/$1
ou=../output/$1

for s in `ls $in`
do
    na=${s%.txt}
    java MainSequence $in/$s $ou $1
    dot -Tpdf "$ou"/"$na"SequenceConceptLatticeBordat.dot > $ou/$na.pdf
done
