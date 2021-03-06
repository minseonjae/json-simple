package seonjae.json.parser;

import seonjae.json.Json;
import seonjae.json.JsonArray;
import seonjae.json.parser.exception.ParseException;
import seonjae.json.parser.obj.ContainerFactory;
import seonjae.json.parser.obj.ContentHandler;
import seonjae.json.parser.obj.Yylex;
import seonjae.json.parser.obj.Yytoken;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonParser {
	public static final int S_INIT=0;
	public static final int S_IN_FINISHED_VALUE=1;
	public static final int S_IN_OBJECT=2;
	public static final int S_IN_ARRAY=3;
	public static final int S_PASSED_PAIR_KEY=4;
	public static final int S_IN_PAIR_VALUE=5;
	public static final int S_END=6;
	public static final int S_IN_ERROR=-1;
	
	private LinkedList handlerStatusStack;
	private Yylex lexer = new Yylex();
	private Yytoken token = null;
	private int status = S_INIT;
	
	private int peekStatus(LinkedList statusStack){
		if(statusStack.size()==0)
			return -1;
		Integer status=(Integer)statusStack.getFirst();
		return status.intValue();
	}
	
    public void reset(){
        token = null;
        status = S_INIT;
        handlerStatusStack = null;
    }
	public void reset(Reader in){
		lexer.yyreset(in);
		reset();
	}
	public int getPosition(){
		return lexer.getPosition();
	}
	public Object parse(String s) throws ParseException{
		return parse(s, (ContainerFactory) null);
	}
	public Object parse(String s, ContainerFactory containerFactory) throws ParseException{
		StringReader in=new StringReader(s);
		try {
			return parse(in, containerFactory);
		} catch(IOException e) {
			throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, e);
		}
	}
	
	public Object parse(Reader in) throws IOException, ParseException{
		return parse(in, (ContainerFactory)null);
	}
	
	public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseException{
		reset(in);
		LinkedList statusStack = new LinkedList();
		LinkedList valueStack = new LinkedList();
		
		try {
			do {
				nextToken();
				switch(status) {
				case S_INIT:
					switch(token.type) {
					case Yytoken.TYPE_VALUE:
						status=S_IN_FINISHED_VALUE;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(token.value);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status=S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(createObjectContainer(containerFactory));
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status=S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(createArrayContainer(containerFactory));
						break;
					default:
						status=S_IN_ERROR;
					}
					break;
					
				case S_IN_FINISHED_VALUE:
					if(token.type==Yytoken.TYPE_EOF) return valueStack.removeFirst();
					else throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
					
				case S_IN_OBJECT:
					switch(token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if(token.value instanceof String) {
							String key=(String)token.value;
							valueStack.addFirst(key);
							status=S_PASSED_PAIR_KEY;
							statusStack.addFirst(new Integer(status));
						} else status=S_IN_ERROR;
						break;
					case Yytoken.TYPE_RIGHT_BRACE:
						if(valueStack.size()>1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status=peekStatus(statusStack);
						} else status=S_IN_FINISHED_VALUE;
						break;
					default:
						status=S_IN_ERROR;
						break;
					}
					break;
					
				case S_PASSED_PAIR_KEY:
					switch(token.type) {
					case Yytoken.TYPE_COLON:
						break;
					case Yytoken.TYPE_VALUE:
						statusStack.removeFirst();
						String key=(String)valueStack.removeFirst();
						Map parent=(Map)valueStack.getFirst();
						parent.put(key,token.value);
						status=peekStatus(statusStack);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						statusStack.removeFirst();
						key=(String)valueStack.removeFirst();
						parent=(Map)valueStack.getFirst();
						List newArray=createArrayContainer(containerFactory);
						parent.put(key,newArray);
						status=S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newArray);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						statusStack.removeFirst();
						key=(String)valueStack.removeFirst();
						parent=(Map)valueStack.getFirst();
						Map newObject=createObjectContainer(containerFactory);
						parent.put(key,newObject);
						status=S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newObject);
						break;
					default:
						status=S_IN_ERROR;
					}
					break;
					
				case S_IN_ARRAY:
					switch(token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						List val=(List)valueStack.getFirst();
						val.add(token.value);
						break;
					case Yytoken.TYPE_RIGHT_SQUARE:
						if(valueStack.size()>1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status=peekStatus(statusStack);
						} else status=S_IN_FINISHED_VALUE;
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						val=(List)valueStack.getFirst();
						Map newObject=createObjectContainer(containerFactory);
						val.add(newObject);
						status=S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newObject);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						val=(List)valueStack.getFirst();
						List newArray=createArrayContainer(containerFactory);
						val.add(newArray);
						status=S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newArray);
						break;
					default:
						status=S_IN_ERROR;
					}
					break;
				case S_IN_ERROR:
					throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
				}
				if(status==S_IN_ERROR) throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
			} while(token.type!=Yytoken.TYPE_EOF);
		} catch(IOException e){
			throw e;
		}
		throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
	}
	
	private void nextToken() throws ParseException, IOException {
		token = lexer.yylex();
		if(token == null) token = new Yytoken(Yytoken.TYPE_EOF, null);
	}
	
	private Map<?, ?> createObjectContainer(ContainerFactory containerFactory) {
		if(containerFactory == null) return new Json();
		Map<?, ?> m = containerFactory.createObjectContainer();
		
		if(m == null) return new Json();
		return m;
	}
	
	private List<?> createArrayContainer(ContainerFactory containerFactory) {
		if(containerFactory == null) return new JsonArray();
		List<?> l = containerFactory.creatArrayContainer();
		
		if(l == null) return new JsonArray();
		return l;
	}
	
	public void parse(String s, ContentHandler contentHandler) throws ParseException {
		parse(s, contentHandler, false);
	}
	
	public void parse(String s, ContentHandler contentHandler, boolean isResume) throws ParseException {
		StringReader in = new StringReader(s);
		try{
			parse(in, contentHandler, isResume);
		} catch(IOException e) {
			throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, e);
		}
	}
	
	public void parse(Reader in, ContentHandler contentHandler) throws IOException, ParseException{
		parse(in, contentHandler, false);
	}
	
	public void parse(Reader in, ContentHandler contentHandler, boolean isResume) throws IOException, ParseException{
		if(!isResume) {
			reset(in);
			handlerStatusStack = new LinkedList();
		} else {
			if(handlerStatusStack == null) {
				isResume = false;
				reset(in);
				handlerStatusStack = new LinkedList();
			}
		}
		
		LinkedList statusStack = handlerStatusStack;	
		
		try {
			do {
				switch(status) {
				case S_INIT:
					contentHandler.startJSON();
					nextToken();
					switch(token.type) {
					case Yytoken.TYPE_VALUE:
						status=S_IN_FINISHED_VALUE;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.primitive(token.value))
							return;
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status=S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.startObject())
							return;
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status=S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.startArray())
							return;
						break;
					default:
						status=S_IN_ERROR;
					}
					break;
					
				case S_IN_FINISHED_VALUE:
					nextToken();
					if(token.type==Yytoken.TYPE_EOF) {
						contentHandler.endJSON();
						status = S_END;
						return;
					} else {
						status = S_IN_ERROR;
						throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
					}
				case S_IN_OBJECT:
					nextToken();
					switch(token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if(token.value instanceof String){
							String key=(String)token.value;
							status=S_PASSED_PAIR_KEY;
							statusStack.addFirst(new Integer(status));
							if(!contentHandler.startObjectEntry(key)) return;
						} else status=S_IN_ERROR;
						break;
					case Yytoken.TYPE_RIGHT_BRACE:
						if(statusStack.size()>1){
							statusStack.removeFirst();
							status=peekStatus(statusStack);
						} else status=S_IN_FINISHED_VALUE;
						if(!contentHandler.endObject()) return;
						break;
					default:
						status=S_IN_ERROR;
						break;
					}
					break;
					
				case S_PASSED_PAIR_KEY:
					nextToken();
					switch(token.type) {
					case Yytoken.TYPE_COLON:
						break;
					case Yytoken.TYPE_VALUE:
						statusStack.removeFirst();
						status=peekStatus(statusStack);
						if(!contentHandler.primitive(token.value)) return;
						if(!contentHandler.endObjectEntry()) return;
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						statusStack.removeFirst();
						statusStack.addFirst(new Integer(S_IN_PAIR_VALUE));
						status=S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.startArray()) return;
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						statusStack.removeFirst();
						statusStack.addFirst(new Integer(S_IN_PAIR_VALUE));
						status=S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.startObject()) return;
						break;
					default:
						status=S_IN_ERROR;
					}
					break;
				
				case S_IN_PAIR_VALUE:
					statusStack.removeFirst();
					status = peekStatus(statusStack);
					if(!contentHandler.endObjectEntry()) return;
					break;
					
				case S_IN_ARRAY:
					nextToken();
					switch(token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if(!contentHandler.primitive(token.value)) return;
						break;
					case Yytoken.TYPE_RIGHT_SQUARE:
						if(statusStack.size()>1) {
							statusStack.removeFirst();
							status=peekStatus(statusStack);
						} else status=S_IN_FINISHED_VALUE;
						if(!contentHandler.endArray()) return;
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status=S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.startObject()) return;
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status=S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						if(!contentHandler.startArray()) return;
						break;
					default:
						status=S_IN_ERROR;
					}
					break;
					
				case S_END:
					return;
					
				case S_IN_ERROR:
					throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
				}
				if(status==S_IN_ERROR) throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
			}while(token.type!=Yytoken.TYPE_EOF);
		} catch(IOException e) {
			status = S_IN_ERROR;
			throw e;
		} catch(ParseException e){
			status = S_IN_ERROR;
			throw e;
		} catch(RuntimeException e){
			status = S_IN_ERROR;
			throw e;
		} catch(Error e){
			status = S_IN_ERROR;
			throw e;
		}
		status = S_IN_ERROR;
		throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
	}
}
