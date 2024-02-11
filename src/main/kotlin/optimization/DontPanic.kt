fun main(){
    val i=java.util.Scanner(System.`in`)
    val v=mutableMapOf<Int,Int>()
    val (_,w,_,e,p)=List(5){i.nextInt()}
    val (_,_,l)=List(3){i.nextInt()}
    repeat(l){v[i.nextInt()]=i.nextInt()}
    while(true){val(c,d)=List(2){i.nextInt()};val r=i.next()
        println(if((e==c&&r=="RIGHT"&&p<d||e==c&&r=="LEFT"&&p>d)||(r=="RIGHT"&&v[c]?:w<d||r=="LEFT"&&v[c]?:0>d))"BLOCK" else "WAIT")}}

