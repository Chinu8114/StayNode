package com.staynode.config;

import com.staynode.property.Property;
import com.staynode.property.PropertyRepository;
import com.staynode.user.User;
import com.staynode.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final PropertyRepository properties;
    private final PasswordEncoder encoder;

    // Real Unsplash photo URLs (free to hotlink). Curated for PG / hostel / apartment context.
    private static final String U = "https://images.unsplash.com/photo-";
    private static final String Q = "?w=1200&q=75&auto=format&fit=crop";

    private static List<String> imgs(String... ids) {
        return java.util.Arrays.stream(ids).map(id -> U + id + Q).toList();
    }

    private static final List<String> P1 = imgs(
            "1522708323590-d24dbb6b0267",  // bedroom
            "1505691938895-1758d7feb511",  // bedroom 2
            "1568605114967-8130f3a36994",  // bathroom
            "1567767292278-a4f21aa2d36e"); // gym
    private static final List<String> P2 = imgs(
            "1631679706909-1844bbd07221",  // hostel
            "1493809842364-78817add7ffb",  // modern bedroom
            "1556909114-f6e7ad7d3136",     // living room
            "1571508601891-ca5e7a713859"); // workspace
    private static final List<String> P3 = imgs(
            "1560448204-e02f11c3d0e2",     // luxury apt
            "1502672260266-1c1ef2d93688",  // apartment
            "1522771739844-6a9f6d5f14af",  // kitchen
            "1567767292278-a4f21aa2d36e"); // gym
    private static final List<String> P4 = imgs(
            "1554995207-c18c203602cb",     // interior
            "1631049307264-da0ec9d70304",  // bed
            "1568605114967-8130f3a36994",  // bath
            "1571508601891-ca5e7a713859"); // study
    private static final List<String> P5 = imgs(
            "1564013799919-ab600027ffc6",  // luxury exterior
            "1560185007-cde436f6a4d0",     // luxury room
            "1582719478250-c89cae4dc85b",  // spa
            "1540518614846-7eded433c457"); // building
    private static final List<String> P6 = imgs(
            "1545324418-cc1a3fa10c00",     // office building
            "1522708323590-d24dbb6b0267",
            "1571508601891-ca5e7a713859",
            "1556909114-f6e7ad7d3136");
    private static final List<String> P7 = imgs(
            "1505691938895-1758d7feb511",
            "1571508601891-ca5e7a713859",
            "1568605114967-8130f3a36994",
            "1522771739844-6a9f6d5f14af");
    private static final List<String> P8 = imgs(
            "1502672260266-1c1ef2d93688",
            "1556909114-f6e7ad7d3136",
            "1554995207-c18c203602cb",
            "1545324418-cc1a3fa10c00");
    private static final List<String> P9 = imgs(
            "1567767292278-a4f21aa2d36e",
            "1522708323590-d24dbb6b0267",
            "1568605114967-8130f3a36994",
            "1571508601891-ca5e7a713859");
    private static final List<String> P10 = imgs(
            "1493809842364-78817add7ffb",
            "1505691938895-1758d7feb511",
            "1522771739844-6a9f6d5f14af",
            "1556909114-f6e7ad7d3136");
    private static final List<String> P11 = imgs(
            "1554995207-c18c203602cb",
            "1556909114-f6e7ad7d3136",
            "1568605114967-8130f3a36994",
            "1545324418-cc1a3fa10c00");
    private static final List<String> P12 = imgs(
            "1522708323590-d24dbb6b0267",
            "1505691938895-1758d7feb511",
            "1571508601891-ca5e7a713859",
            "1568605114967-8130f3a36994");

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            log.info("Seed skipped — users already exist");
            return;
        }
        log.info("Seeding demo users and properties...");

        User user = users.save(User.builder()
                .name("Arjun Sharma").email("user@demo.com").password(encoder.encode("demo123"))
                .phone("+91 98765 00001").role(User.Role.USER).build());

        User owner1 = users.save(User.builder()
                .name("Rajesh Kumar").email("owner@demo.com").password(encoder.encode("demo123"))
                .phone("+91 98765 43210").role(User.Role.OWNER).build());

        User owner2 = users.save(User.builder()
                .name("Priya Sharma").email("priya@demo.com").password(encoder.encode("demo123"))
                .phone("+91 87654 32109").role(User.Role.OWNER).build());

        User owner3 = users.save(User.builder()
                .name("Meera Nair").email("meera@demo.com").password(encoder.encode("demo123"))
                .phone("+91 98700 11223").role(User.Role.OWNER).build());

        User owner4 = users.save(User.builder()
                .name("Arun Bhatia").email("arun@demo.com").password(encoder.encode("demo123"))
                .phone("+91 11223 44556").role(User.Role.OWNER).build());

        users.save(User.builder()
                .name("StayNode Admin").email("admin@demo.com").password(encoder.encode("demo123"))
                .phone("+91 00000 00000").role(User.Role.ADMIN).build());

        properties.saveAll(List.of(
                Property.builder().title("Premium Boys PG — Koramangala 5th Block").city("Bangalore")
                        .location("Koramangala 5th Block, Near Forum Mall").lat(12.9352).lng(77.6245)
                        .price(12000).type(Property.Type.PG).gender(Property.Gender.MALE)
                        .rating(4.8).reviews(124).availSlots(3)
                        .amenities(List.of("wifi","ac","food","security","laundry","tv","parking"))
                        .rules(List.of("No smoking or alcohol","Guests allowed till 9 PM","Rent due by 5th"))
                        .images(P1)
                        .description("Spacious, well-maintained PG for working professionals in the heart of Koramangala.")
                        .depositMonths(2).featured(true).verified(true).owner(owner1).build(),

                Property.builder().title("Girls Hostel Near IT Park — Whitefield").city("Bangalore")
                        .location("Whitefield, Near EPIP Zone").lat(12.9698).lng(77.7499)
                        .price(9500).type(Property.Type.Hostel).gender(Property.Gender.FEMALE)
                        .rating(4.6).reviews(87).availSlots(6)
                        .amenities(List.of("wifi","food","security","laundry","cctv"))
                        .rules(List.of("Women only","Curfew at 11 PM","ID proof mandatory"))
                        .images(P2)
                        .description("Safe and comfortable hostel for working women near Whitefield IT hub.")
                        .depositMonths(2).featured(true).verified(true).owner(owner1).build(),

                Property.builder().title("Co-living Studio Apartment — Baner, Pune").city("Pune")
                        .location("Baner, Near Hinjewadi IT Park").lat(18.5590).lng(73.7868)
                        .price(18000).type(Property.Type.Flat).gender(Property.Gender.ANY)
                        .rating(4.9).reviews(203).availSlots(2)
                        .amenities(List.of("wifi","ac","gym","parking","security","laundry","tv"))
                        .rules(List.of("No pets","Pre-approval for guests"))
                        .images(P3)
                        .description("Modern co-living studio apartments designed for working professionals.")
                        .depositMonths(2).featured(true).verified(true).owner(owner2).build(),

                Property.builder().title("Budget PG — HSR Layout Sector 1").city("Bangalore")
                        .location("HSR Layout Sector 1, Near BDA Complex").lat(12.9116).lng(77.6389)
                        .price(7500).type(Property.Type.PG).gender(Property.Gender.MALE)
                        .rating(4.2).reviews(45).availSlots(8)
                        .amenities(List.of("wifi","food","security"))
                        .rules(List.of("No cooking in rooms","Rent by 5th of month"))
                        .images(P4)
                        .description("Affordable and clean PG perfect for students and fresh graduates.")
                        .depositMonths(1).featured(false).verified(true).isNew(true).owner(owner2).build(),

                Property.builder().title("Luxury Girls PG — Andheri West").city("Mumbai")
                        .location("Andheri West, Near Infinity Mall").lat(19.1364).lng(72.8296)
                        .price(22000).type(Property.Type.PG).gender(Property.Gender.FEMALE)
                        .rating(4.9).reviews(156).availSlots(1)
                        .amenities(List.of("wifi","ac","food","security","laundry","gym","cctv","tv","parking"))
                        .rules(List.of("Women only","Strict visitor policy"))
                        .images(P5)
                        .description("Boutique luxury PG for women with 5-star amenities.")
                        .depositMonths(3).featured(true).verified(true).owner(owner3).build(),

                Property.builder().title("Working Professionals Hostel — Noida").city("Noida")
                        .location("Sector 62, Near HCL Technologies").lat(28.6272).lng(77.3775)
                        .price(11000).type(Property.Type.Hostel).gender(Property.Gender.ANY)
                        .rating(4.4).reviews(67).availSlots(5)
                        .amenities(List.of("wifi","ac","food","gym","parking"))
                        .rules(List.of("No loud parties"))
                        .images(P6)
                        .description("Modern co-working and co-living hostel for tech professionals.")
                        .depositMonths(2).featured(false).verified(true).owner(owner3).build(),

                Property.builder().title("Student PG Near Campus — Hauz Khas").city("Delhi")
                        .location("Hauz Khas Village, Near Delhi University").lat(28.5521).lng(77.1874)
                        .price(8500).type(Property.Type.PG).gender(Property.Gender.ANY)
                        .rating(4.0).reviews(38).availSlots(10)
                        .amenities(List.of("wifi","food","laundry"))
                        .rules(List.of("Rent by 5th of month"))
                        .images(P7)
                        .description("Budget-friendly PG ideal for students near Delhi University and IIT Delhi.")
                        .depositMonths(1).featured(false).verified(true).owner(owner4).build(),

                Property.builder().title("Premium Co-Living — Viman Nagar, Pune").city("Pune")
                        .location("Viman Nagar, Near Airport").lat(18.5642).lng(73.9199)
                        .price(15500).type(Property.Type.Flat).gender(Property.Gender.ANY)
                        .rating(4.7).reviews(92).availSlots(3)
                        .amenities(List.of("wifi","ac","security","tv","parking"))
                        .rules(List.of("No smoking inside"))
                        .images(P8)
                        .description("Premium co-living flats in the upscale Viman Nagar locality.")
                        .depositMonths(2).featured(true).verified(true).isNew(true).owner(owner2).build(),

                Property.builder().title("Boys PG with Gym — Gachibowli, Hyderabad").city("Hyderabad")
                        .location("Gachibowli, Near DLF Cybercity").lat(17.4447).lng(78.3520)
                        .price(10500).type(Property.Type.PG).gender(Property.Gender.MALE)
                        .rating(4.3).reviews(53).availSlots(4)
                        .amenities(List.of("wifi","ac","gym","parking","security"))
                        .rules(List.of("No alcohol","Gym hours 6 AM – 10 PM"))
                        .images(P9)
                        .description("Modern PG facility for male professionals with fully equipped gym.")
                        .depositMonths(2).featured(false).verified(true).owner(owner4).build(),

                Property.builder().title("Affordable Girls Hostel — Velachery, Chennai").city("Chennai")
                        .location("Velachery, Near Phoenix Market City").lat(12.9802).lng(80.2196)
                        .price(7000).type(Property.Type.Hostel).gender(Property.Gender.FEMALE)
                        .rating(4.1).reviews(29).availSlots(12)
                        .amenities(List.of("wifi","food","security","laundry"))
                        .rules(List.of("Women only","Curfew 10:30 PM"))
                        .images(P10)
                        .description("Clean and safe hostel for women in prime Velachery location.")
                        .depositMonths(1).featured(false).verified(true).owner(owner4).build(),

                Property.builder().title("Furnished Single Rooms — Koramangala 4th Block").city("Bangalore")
                        .location("4th Block Koramangala, Near Sony World Signal").lat(12.9340).lng(77.6290)
                        .price(14000).type(Property.Type.Flat).gender(Property.Gender.ANY)
                        .rating(4.6).reviews(71).availSlots(2)
                        .amenities(List.of("wifi","ac","security","tv","laundry","parking"))
                        .rules(List.of("No subletting"))
                        .images(P11)
                        .description("Fully furnished single-occupancy rooms with private balcony.")
                        .depositMonths(2).featured(true).verified(true).owner(owner1).build(),

                Property.builder().title("Ultra-Budget Boys PG — Madhapur").city("Hyderabad")
                        .location("Madhapur, Near Cyber Towers").lat(17.4486).lng(78.3908)
                        .price(5500).type(Property.Type.PG).gender(Property.Gender.MALE)
                        .rating(3.9).reviews(19).availSlots(15)
                        .amenities(List.of("wifi","food"))
                        .rules(List.of("Strict 11 PM curfew"))
                        .images(P12)
                        .description("The most affordable PG option near Hyderabad's Cyber Towers.")
                        .depositMonths(1).featured(false).verified(false).isNew(true).owner(owner3).build()
        ));

        log.info("Seed complete — {} users, {} properties", users.count(), properties.count());
    }
}
