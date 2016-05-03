//Julian Eschrich, s4juesch@uni-trier.de, Mnr.: 1087526

package edu.uap.tram;

import java.util.Stack;

//Aufgabe 1

public class AbstractMachine {

	Stack<Integer> stack;
	public int PC, PP, FP, TOP;
	public boolean debug;
	
	AbstractMachine(){
		stack = new Stack<Integer>();
		this.PC = -1;
		this.PP = -1;
		this.FP = -1;
		this.TOP = -1;	
	}
	
	public void constant(int k){
		stack.push(k);
		TOP = TOP + 1;
		PC  = PC + 1;
	}
	
	public void store(int k){
		stack.set(PP + k, stack.elementAt(TOP));
		stack.pop();
		TOP = TOP - 1;
		PC = PC + 1;
	}
	
	public void store(int k, int d){
		stack.set(spp(d, PP, FP)+k, stack.elementAt(TOP));
		stack.pop();
		TOP = TOP - 1;
		PC = PC + 1;
	}
	
	public void load (int k){
		stack.push(stack.elementAt(PP+k));
		TOP = TOP+1;
		PC = PC+1;
	}
	
	public void load(int k, int d){
		stack.push(stack.elementAt(spp(d,PP,FP)+k));
		TOP = TOP+1;
		PC=PC+1;
	}
	
	public void add(){
		stack.set(TOP-1,stack.elementAt(TOP-1) + stack.elementAt(TOP));
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
	}
	
	public void sub(){
		stack.set(TOP-1,stack.elementAt(TOP-1) - stack.elementAt(TOP));
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
		}
	
	public void mul(){
		stack.set(TOP-1,stack.elementAt(TOP-1) * stack.elementAt(TOP));
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
		}
	
	public void div(){
		stack.set(TOP-1,stack.elementAt(TOP-1) / stack.elementAt(TOP));
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
		}
	
	public void lt(){
		if(stack.elementAt(TOP-1) < stack.elementAt(TOP)){
			stack.set(TOP-1, 1);
		
		}
		else
			stack.set(TOP-1,0);
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
	}
	
	public void gt(){
		if(stack.elementAt(TOP-1) > stack.elementAt(TOP)){
			stack.set(TOP-1, 1);
		}
		else
			stack.set(TOP-1,0);
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
	}
	
	public void eq(){
		if(stack.elementAt(TOP-1) == stack.elementAt(TOP)){
			stack.set(TOP-1, 1);
		}
		else
			stack.set(TOP-1,0);
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
	}
	
	public void neq(){
		if(stack.elementAt(TOP-1) != stack.elementAt(TOP)){
			stack.set(TOP-1, 1);
		}
		else
			stack.set(TOP-1,0);
		stack.pop();
		TOP=TOP-1;
		PC=PC+1;
	}
	
	public void goTO(int p){
		PC = p;
	}
	
	public void ifzero(int p){
		if(stack.elementAt(TOP)==0){
			PC=p;
		}
		else{
			PC = PC+1;
		}
		stack.pop();
		TOP=TOP-1;
		
	}
	
	public void halt(){
		PC=-1;
	}
	
	public void nop(){
		PC=PC+1;
	}
	
	public int spp(int d, int pp, int fp){
		if(d == 0) return pp;
		else spp(d-1, stack.elementAt(fp+2),stack.elementAt(fp+3));
		return -1;
	}
	
	public int sfp(int d, int pp, int fp){
		if(d==0) return fp;
		else sfp(d-1, stack.elementAt(fp+2),stack.elementAt(fp+3));
		return -1;
	}
	
	public void invoke(int n, int p, int d){
		stack.push(PP);
		stack.push(FP);
		stack.push(spp(d,PP,FP));
		stack.push(sfp(d,PP,FP));
		stack.push(PC+1);
		PP=TOP-n+1;
		FP=TOP+1;
		TOP=TOP+5;
		PC=p;
	}
	
	public void ret(){
		int res_temp;
		res_temp=stack.elementAt(TOP);
		TOP=PP;
		PP=stack.elementAt(FP);
		PC=stack.elementAt(FP+4);
		FP=stack.elementAt(FP+1);
		stack.set(TOP, res_temp);
	}
	
	public void run(Instruction[] instArray, boolean debug){
		while(PC>=0){
			execute(instArray[PC],debug);
		}
	}
	
	public void execute(Instruction inst, boolean debug){
		switch(inst.getOpcode()){
			case Instruction.CONST:
				constant(inst.getArg1());
				break;
			case Instruction.LOAD:
				load(inst.getArg1(),inst.getArg2());
				break;
			case Instruction.STORE:
				store(inst.getArg1(),inst.getArg2());
				break;
			case Instruction.ADD:
				add();
				break;
			case Instruction.SUB:
				sub();
				break;
			case Instruction.MUL:
				mul();
				break;
			case Instruction.DIV:
				div();
				break;
			case Instruction.LT:
				lt();
				break;
			case Instruction.GT:
				gt();
				break;
			case Instruction.EQ:
				eq();
				break;
			case Instruction.NEQ:
				neq();
				break;
			case Instruction.IFZERO:
				ifzero(inst.getArg1());
				break;
			case Instruction.GOTO:
				goTO(inst.getArg1());
				break;
			case Instruction.HALT:
				halt();
				break;
			case Instruction.NOP:
				nop();
				break;
			case Instruction.INVOKE:
				invoke(inst.getArg1(), inst.getArg2(), inst.getArg3());
				break;
			case Instruction.RETURN:
				ret();
				break;
			default:
				break;
		}
		if(debug)printStack();
	}
	
	//Aufgabe 2
	
	public void printStack(){
		
		System.out.println("||| POINTER: PC="+PC+" ||| PP="+PP+" ||| FP="+FP+" ||| TOP="+TOP+" |||");
		for(int i=TOP;i>=0;i--){
			System.out.println("||| STACK["+i+"] = "+stack.elementAt(i));
		}
	}
}