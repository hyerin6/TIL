# Internet Address 



## 1. InetAddress 클래스 

InetAddress 클래스는 IP 주소에 해당하는 Java 클래스이다. 

IPv4, IPv6 무관하게 동일한 구현이 가능하다. 

네트워크 통신을 시작하려면, 먼저 통신 상대 host의 IP 주소에 해당하는 InetAddress 객체를 생성해야 한다. 

<br />

### 1) InetAddress 객체 생성 

```java
public static void main(String[] args) {
    String name = "www.skhu.ac.kr";
    try {
        InetAddress address = InetAddress.getByName(name);
        System.out.println(address);
    } catch (UnknownHostException ex) {
        System.out.println("Could not find " + name);
    }
}
```

hostname은 사람을 위한 것이고 통신을 위해 IP 주소가 필요하다. 

IP 주소를 알려주는 서버들이 인터넷에 있다. 이런 서버를 DNS라고 부른다. 

`getByName` 메소드는 파라미터 값으로 주어진 hostname을 DNS 서버에게 물어보고 IP 주소를 받는다. 

그리고 hostname과 IP 주소가 채워진 InetAddress 객체를 리턴한다. 

<br />


```java
public static void main(String[] args) {
    String name = "203.246.75.30";
    try {
        InetAddress address = InetAddress.getByName(name);
        System.out.println(address);
        System.out.println(address.getCanonicalHostName());
    } catch (UnknownHostException ex) {
        System.out.println("Could not find " + name);
    }
}
```


위와 같이 `getByName`을 호출하면 여러가지 이유로 정확한 hostname을 받아오지 못하는 경우가 많다. 

"203.246.75.30"는 서버의 IP 주소이다. 

IP 주소 하나에 여러 hostman이 할당될 수 있고, 반대로 hostname 하나에 여러 IP 주소가 할당될 수 있다. 

예를들어 `www.naver.com`에 IP 주소 두 개가 할당되어 있다. 

`InetAddress.getAllByName("hostname")` 메소드는

hostname에 할당된 IP 주소 목록을 InetAddress[] 배열로 리턴한다.


<br />


```java
public static void main(String[] args) {
    try {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println(address);
    } catch (UnknownHostException ex) {
        System.out.println("Could not find this computer's address");
    }
}
```

`InetAddress.getLocalHost()` 메소드는 현재 메소드가 실행되고 있는 localhost의 hostname과 

IP 주소가 채워진 InetAddress 객체를 리턴한다.   

<br />

```java
public static void main(String[] args) {
    try {
        byte[] a = new byte[] { (byte)203, (byte)246, 75, 30 };
        InetAddress address = InetAddress.getByAddress(a);
        System.out.println(address);
    } catch (UnknownHostException ex) {
        System.out.println("Could not find this computer's address");
    }
}
```

`InetAddress.getByAddress()` 메소드는 byte[] 타입으로 주어진 IP 주소로 InetAddress 객체를 생성한다. 

이 메소드는 DNS 서버에 이 IP 주소를 물어보지 않기 때문에 DNS 서버에 물어보는 `getByName()` 메소드보다 빠르다. 

<br />
<br />

### 2) Getter 메소드 

```java
public static void main(String[] args) {
    String name = "www.skhu.ac.kr";
    try {
        InetAddress address = InetAddress.getByName(name);
        System.out.println(address);
        System.out.println(address.getHostName());
        System.out.println(address.getCanonicalHostName());
        System.out.println(Arrays.toString(address.getAddress()));
        System.out.println(address.getHostAddress());
    } catch (UnknownHostException ex) {
        System.out.println("Could not find " + name);
    }
}
```

* `InetAddress.getHostName()` : 내부에 채워져 있던 hostname 값을 리턴한다. 
* `InetAddress.getCanonicalHostName()`: DNS 서버에 hostname을 물어봐서 리턴한다. 따라서 조금 느리다. 
* `InetAddress.getAddress()`: 내부에 채워져 있던 IP 주소를 byte[] 타입으로 리턴한다. 
* `InetAddress.getHostAddress()`: 내부에 채워져 있던 IP 주소를 String 타입으로 리턴한다. 

<br /> 
<br /> 

### 3) Testing Rechability  
 
```java
public static void main(String[] args) {
    String name = "www.skhu.ac.kr";
    try {
        InetAddress address = InetAddress.getByName(name);
        System.out.println(address);
        System.out.println(address.isReachable(2000));
    } catch (UnknownHostException ex) {
        System.out.println("Could not find " + name);
    } catch (IOException e) {
        System.out.println("Is not reachable");
    }
}
```


InetAddress 클래스의 isReachable 메소드는 그 IP 주소의 호스트가 응답을 하는지 확인해서 true/false를 리턴한다.

ICMP 프로토콜 echo request를 호스트에 보내서, 응답이 오는지 확인한다.

그런데 이 ICMP 프로토콜 패킷이 방화벽이나 라우터에서 차단될 수 있어서,
호스트가 살아있음에도 false를 리턴하는 경우가 많다.

즉 호스트가 살아있는지 확인하는 용도로
InetAddress 클래스의 isReachable 메소드를 사용할 수 없다.

동일한 LAN (Local Area Network) 환경의 PC들 끼리는 비교적 잘 작동한다.

<br /> 



## 2. NetworkInterface 클래스

localhost에 네트워크 인터페이스 장치(랜카드나 WIFI 장치)가 복수개인 경우에, 통신에 사용할 장치를 선택해야 한다.


<br />

### 1) 네트워크 인터페이스 목록 

```java
public static void main(String[] args) throws SocketException {
    Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
    while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        System.out.println(ni);
    }
}
```

서버에는 네트워크 인터페이스가 여러 개 장착되어있을 수 있다.

물리적인 네트워크 인터페이스 장치에는, 유선 랜카드나 무선 WIFI 장치가 있다.



SW로 구현된 가상(virtual)의 네트워크 인터페이스 장치들도 많다.

NetworkInterface.getNetworkInterfaces() 메소드는
localhost의 네트워크 인터페이스 장치 목록을 리턴한다.



위 코드의 출력은 내 PC의 네트워크 인터페이스 목록이다.

이 중에서 eth2 이름의 장치가 유선 랜카드 장치이다.

<br />
<br />

### 2) Factory 메소드 

```java
public static void main(String[] args) throws SocketException {
    NetworkInterface ni = NetworkInterface.getByName("eth7");
    
    System.out.println(Arrays.toString(ni.getHardwareAddress()));
    
    for (byte b : ni.getHardwareAddress())
        System.out.printf("%02x ", b);
    System.out.println();
    
    Enumeration<InetAddress> addresses = ni.getInetAddresses();
    while (addresses.hasMoreElements())
        System.out.println(addresses.nextElement());
 }
```


이 예제는, "eth7" 이름의 네트워크 인터페이스 장치의 MAC 주소와 IP 주소 목록을 출력한다.

<br />  

* `NetworkInterface ni = NetworkInterface.getByName("eth7");`
   
장치 이름으로 NetowkrInterface 객체를 생성한다.
이 객체이는 네트워크 인터페이스 장치에 대한 정보가 들어있다.


* `NetworkInterface.getHardwareAddress()`
   
네트워크 인터페이스 장치의 MAC 주소를 byte[] 타입으로 리턴한다.


* `NetworkInterface.getInetAddresses()`
   
네트워크 인터페이스 장치에 할당된 IP 주소 목록을 리턴한다.


<br />

