package CreationalDesignPatterns;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;

 class Singleton {
	//singleton instance created
	public static final Singleton instance = new Singleton();
		//private constructor to avoid client application to use constructor 
	private Singleton(){}
	//Public static method that returns the instance of the class,global access point for outer world to get the instance of the singleton class.
	public static Singleton getInstance(){
		return instance;
	}	
}
 //thread safe plus kinda lazy singleton
 class threadSafeSingleton {
		//singleton instance created
		public static threadSafeSingleton instance;
		//private constructor to avoid client application to use constructor 
		private  threadSafeSingleton() {
			// TODO Auto-generated constructor stub
		}
		//Public static method that returns the instance of the class,global access point for outer world to get the instance of the singleton class.
		public static synchronized threadSafeSingleton getInstance(){
			if(instance == null){
				instance = new threadSafeSingleton();
			}
			return instance;
		}		
	}
//double checked locking...during the multiple thread performs
 class threadlockSafeSingleton {
		//singleton instance created
		public static threadlockSafeSingleton instance;
		//private constructor to avoid client application to use constructor 
		private  threadlockSafeSingleton() {
			// TODO Auto-generated constructor stub
		}
		//Public static method that returns the instance of the class,global access point for outer world to get the instance of the singleton class.
		public static synchronized threadlockSafeSingleton getInstance(){
			if(instance == null){
				synchronized (threadlockSafeSingleton.class) {			
					if(instance == null){
						instance = new threadlockSafeSingleton();
					}
				}
			}
			return instance;
		}
	} 
 //using inner static helper class ...Bill Push Singleton Implementation.
   class BillPughSingleton {
	 private BillPughSingleton(){}
	 private static class SingletonHelper{
	 private static final BillPughSingleton INSTANCE = new
	 BillPughSingleton();}
	 public static BillPughSingleton getInstance(){
	 return SingletonHelper.INSTANCE;
	 }}

   //Reflection to destroy the Singleton
   class ReflectionSingletonTest{
	
	   public static void main(String [] args){
		   Singleton instanceOne = Singleton.getInstance();
		   Singleton instanceTwo = null;
		   
		   try{
			   Constructor[] costructors = Singleton.class.getDeclaredConstructors();
			   
			   for(Constructor constructor : costructors){
				   constructor.setAccessible(true);
				   instanceTwo = (Singleton) constructor.newInstance();
				   break;
			   }
		   }catch(Exception e){
			   e.printStackTrace();
		   }
	
		   System.out.println(instanceOne.hashCode());
		   System.out.println(instanceTwo.hashCode());
		   //2018699554,1311053135
		   //Basically both instances are not same.
		   //which destroys the functionalities of the potential single pattern 
	   }}
   
   //Most distributed System  
   class SerializedSingleton implements Serializable{
	 
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	   
	   private SerializedSingleton(){};
	   
	   private static class SingletonHelper{
		   private static final SerializedSingleton instance = new SerializedSingleton();
		   
	   }
	
	   public static SerializedSingleton getInstance(){
		   return SingletonHelper.instance;
	   }
	   
	   //https://docs.oracle.com/javase/6/docs/platform/serialization/spec/input.html for more I guess
	   //when we deserialize will create the new instance so we need to protect 
	   //remove this content then there would problem ...it will destroy functionality of the Singleton
	   //eadResolve method allows a class to replace/resolve the object read from the stream before it is returned to the caller. By implementing the readResolve method, a class can directly control the types and instances of its own instances being deserialized. The method is defined as follows:
	   protected Object readResolve(){
		   return getInstance();
	   }
   }
   
   class SerializedSingletonTest{
	   
	   public static void main(String [] args)throws Exception{
		   SerializedSingleton instanceOne = SerializedSingleton.getInstance();
		   
		   ObjectOutput out = new ObjectOutputStream(new FileOutputStream("test.ser"));
		   out.writeObject(instanceOne);
		   out.close();
		   
		   ObjectInput input = new ObjectInputStream(new FileInputStream("test.ser"));
		   SerializedSingleton instanceTwo=(SerializedSingleton) input.readObject();
		   input.close();
		   
		   System.out.println(instanceOne.hashCode());
		   System.out.println(instanceTwo.hashCode());
		
	//	   1028566121
//		   1028566121
	   }
   }