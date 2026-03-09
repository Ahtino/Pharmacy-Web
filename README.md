<h2>Pharmacy Web (Pharmacy Management)</h2>
<p>This project is about Pharmacy Web, where we can organize the medicine flow by stock in & out, also we can alert the pharmacist about the expiration date of the medicine to the Pharmacist</p>

<h2>How to Deploy:</h2>
<p>First of all, we need to deploy a database, which is PostgreSQL</p>
<p> Then deploy the repository of the springboot project that has already been pushed to github but for the first time, mostly it will crash</p>
<p> So we need to fix it based on the problem it tells us in the Railway. First i need to change the code in application.properties: localhost to ${PGHOST}, serverport to ${PGPORT}, name of database to ${PGDATABASE}, this for url, and then change username to ${PGUSER} and password to ${PGPASSWORD}</p>
<p>next we need to copy the value of PGDATABASE, PGHOST(copy URL from public network domain), PGPORT(number in URL from public network domain), PGPASSWORD, PGUSER from a variable in the PostgreSQL database, and paste it into a variable of springboot project</p>
<p>then deploy it and it will succeed </p>
<p> If it fails to deploy u need to create a file named mise.toml to have the same environment as railway, and I use maven, so I need to change my maven version to 3.9.9</p>
<p>Also, Railway only support java 17 or 21. If you use Java 25, it might crash, but I used maven thaty doesn't matter.<\p>
<p>for java u need to use version 21.0.2</p>
<br>
  
<h2> What I use in this project:</h2>
<p>- Railway for deploy</p>
<p>- IDE for SpringBoot</p>
<p>- PostgreSQL for Database</p>
<p>- In Spring Boot, I use Thymeleaf, Lombok, Spring Web, Spring Security, Spring Data JPA, PostgreSQL Driver</p>

<h2>Link to Website:</h2><br>
<a href="https://pharmacy-web-production.up.railway.app/">Link</a>
<h2>Data Base Figure:</h2>

@Table(name = "medicine")
@Entity
@Data
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private LocalDate importDate;
    private LocalDate expiryDate;
    private Long pharmacyId;}
<br>
    
@Table(name = "pharmacy")
@Entity
@Data
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String passwordHash;

    @Transient
    private String password; }

