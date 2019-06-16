package com.gdut.xg.shop.apriori;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;

public class Apriori {

	public static final double support=0.1;
	private static final double confidence=0.3;
	private static Map<String,Integer> record=null;
	//输入map<list,int>,返回map<list,int>
	//String []为商品编号，record为出现次数
	public static Map<String,Double> getResult(Map<String ,Integer> record,Map<String,Integer> init,HashSet  p
){
		//第一步，此时的数据状态：list(商品id;一个),出现次数
		Apriori.record=record;

		Map<String,Integer> pre=null;
		//初始状态为每个商品及其总数。
	Map<String,Integer> first=init;
	int time=0;
	do {
		pre=init;
		init=countSupport(init,p);
	++time;
	}while(time<1);
	//然后对每个商品计算其置信度
	Map<String,Integer> result=null;
	Map<String,Double> strongRule=new HashMap<>();
		if(init.size()==0) {
			//此时pre为符合支持度的结果集，形式为 A,B,D 2 
			//然后要计算符合置信度的结果集
			//1.求其子集
			result=pre;
			
		}else {
			result=init;
		}
		for(String g:result.keySet()) {
			String[] t=g.split(",");
			for(int i=0;i<t.length;i++) {
				for(int q=i+1;q<t.length;q++) {
					String zuhe=t[i]+","+t[q];
					int count=0;
					for(String x:Apriori.record.keySet()) {
						if(x.contains(zuhe)) {
							count+=Apriori.record.get(x);
						}
					}
					if(count!=0) {
						int check=0;
						for(String b:first.keySet()) {
							if(b.equals(t[i])) {
								BigDecimal x=new BigDecimal(first.get(b).intValue());
								Double re=new BigDecimal(count).divide(x,2,BigDecimal.ROUND_HALF_UP).doubleValue();
								//大于置信度
								if(re>confidence) {
									System.out.printf("强规则: %s --> %s,置信度: %s %s",t[i],t[q],re,"\n");
									strongRule.put(t[i]+","+t[q], re);

								}
								check+=1;
							}
							if(b.equals(t[q])) {
								BigDecimal x=new BigDecimal(first.get(b).intValue());
								Double re=new BigDecimal(count).divide(x,2,BigDecimal.ROUND_HALF_UP).doubleValue();
								//大于置信度
								if(re>confidence) {
									System.out.printf("强规则: %s --> %s,置信度: %s %s",t[q],t[i],re,"\n");
									strongRule.put(t[q]+","+t[i], re);

								}
								check+=1;
							}
							if(check==2) {
								break;
							}
						}	
					}
					
				}
			}
		}
		return strongRule;
		
		
		
	} 
	
	//先链接，并计算支持度，而后剪枝
	private static Map<String,Integer> countSupport(Map<String,Integer> count,HashSet<String> notContain){

		Map<String,Integer> map=new HashMap<String, Integer>();
		HashSet<String> notContain2=new HashSet<String>();
		for(String  s:count.keySet()) {
			
			for(String  q:count.keySet()) {
				if(q.equals(s)) {
					continue;
				}
				HashSet<String> hashset=new HashSet<String>();
				String [] temp=s.split(",");
				String [] temp2=q.split(",");
				Arrays.sort(temp);
				Arrays.sort(temp2);
			//如果有一项不相等，则可以链接
				int connect=0;
				
					for(int i=0;i<temp.length;i++) {
						if(!temp[i].equals(temp2[i])) {
							connect++;
						}
					}
				if(connect==1) {
					for(int size=0;size<temp2.length;size++) {
						hashset.add(temp2[size]);
						hashset.add(temp[size]);
					}
				}
				//此时hashset里面为l1,l2的剪枝
				if(hashset.size()>0) {
					//比如 ab,cd,ef
					String []p2= {};
					String []p=hashset.toArray( p2);

					
					Arrays.sort(p);
					if(hashset.size()==temp.length+1) {
						Boolean countSupport=true;
						//notContain 为 d,e,q
						for(String not:notContain) {
							String[] notarray=not.split(",");
							Arrays.sort(notarray);
							String p1="";
							for(String l:p) {
								p1+=l;
								
							}
							String p3="";
							for(String x:notarray) {
								p3+=x;
							}
							if(p1.contains(p3)) {
								countSupport=false;
							}
						}
						//计算支持度，如果小于阈值则不加入Map
						if(countSupport) {
							int support=0;
							for(String  l:Apriori.record.keySet()) {
								//此时的l已经排序,可能为 a,d,e,f为一个l
								Integer count2=0;
								for(String i:p) {
									String []larray=l.split(",");
									for(String x:larray) {
										if(i.equals(x)) {
											count2++;
										}
									}
								}
								if(count2++==p.length) {
									support+=record.get(l);
								}
							}
							//计算支持度结束
							StringBuilder str=new StringBuilder();
							for(String pstr:p) {
								str.append(pstr+",");
							}
							str.deleteCharAt(str.length()-1);
							if(support>=Apriori.support) {
								
								map.put(str.toString(), support);
							}else {
								//把不符合支持度的加进去
								notContain2.add(str.toString());
							}
							
						}
						
					}
				}
				
			}
			
		}
		notContain.clear();
		notContain.addAll(notContain2);
		return map;
	}
	
}
