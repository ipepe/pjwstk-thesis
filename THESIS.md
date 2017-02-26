# Pytanie:
### Czy Warszawa posiada wystarczającą ilość zgeolokalizowanych punktów dostępu bezprzewodowego internetu aby nawigować po niej bez użycia GPSu?

## Aplikacja Railsowa

 * Problemy z serializacją obiektów (WifiService) w Railsach (wydajnościowe)
 * Sposób przetrworzenia i serwowania danych dla heatmapy
 * Problemy struktury przechowywania i przetwarzania danych na serwer
 * Problem lokalizowania accesspointa na podstawie posiadanych danych (obrazki), mapowanie korzystając z jednego odbiornika na podstawie zmiany sygnału na zaznaczonej trasie
	 * Proste mapowanie
	 * Srednie
	 * Moje
 * Development aplikacji androidowej na podstawie fork'a z opracowanej od dawna aplikacji i podobnych wymaganiach funkcjonalnych (raczej nie)

## Bazy danych

 * Porównanie ile jest zmapowanych sieci w Warszawie do np innych miast?
 * Porównanie ile sieci zostało wykorzystanych z całej puli?
 * Porównanie jak często dumpy mają powtarzające się dane?


## Aplikacja Android
 * Co wiadomo o sieci Wifi w Androidzie? klasa ScanResult (rzeczywistość na przykładzie Nexus 5+ dokumentacja)
 * Jak szybko można skanować sieci Wifi w Androidzie?
		* Moreover, you still need to scan the 12 Wi-fi channels to be sure that you are detecting all access points. An access point broadcasts a beacon every 100ms * 12 channels = 1.2 seconds. Spending less time than that and you risk missing access points. (z http://stackoverflow.com/questions/9533476/increasing-wifi-scan-rate)


## TODO
 * napisanie aplikacji androidowej
 * interfejs wigle neta

# Data collection

* 3 w nocy z soboty na niedziele
* tydzien pozniej o tej samej porze
* sroda kolo 12

# Wyrzucac accespointy ktore sa telefonami/androidami/iosami ale tez Huawei MiFi


# adb wifi connection

## Mac OS
~/android-sdk-macosx/platform-tools/adb kill-server
~/android-sdk-macosx/platform-tools/adb devices
~/android-sdk-macosx/platform-tools/adb tcpip 5555
~/android-sdk-macosx/platform-tools/adb connect 192.168.1.120:5555

## Windows
"C:\Program Files (x86)\Android\android-sdk\platform-tools\adb" kill-server
"C:\Program Files (x86)\Android\android-sdk\platform-tools\adb" devices
"C:\Program Files (x86)\Android\android-sdk\platform-tools\adb" tcpip 5555
"C:\Program Files (x86)\Android\android-sdk\platform-tools\adb" connect 192.168.1.120:5555

# Apliakacja Androidowa
 * Android Studio v2.3.1
 * Gradle 2.1.3
 * com.michaelpardo:activeandroid:3.1.0

Wybrałem bibliotekę ActiveAndroid jako element Object-Relational Mapping (ORM) ze względu na swoje podobieństwo do ActiveRecord ze świata języka programowania Ruby oraz ze względu na użycie bazy SQLite która jest popularnym rozwiązaniem na Androidzie.

Lista wersji androida sdk i popularny kod.


W pierwszym etapie, skupiłem się na skanowaniu wifi oraz na pracy z (Klasa skanująca) oraz ScanResult z pakietu wifi. Zastosowałem prosty linear layout w wersji horyzontalnej. Gdy już miałem wyniki skanowania, to stwierdziłem że trzeba je zapisać w bazie danych korzystając z biblioteki ActiveAndroid. Gdy już skanowanie oraz zapis do bazy danych mamy z głowy, to możemy przejść do bieżącej lokalizacji. W tej kwestii skorzystałem z SmartLocation w trybie pracy Navigate oraz continious. Trzeba pamiętać że takie ustawienie jest niekorzystne dla czasu pracy na baterii. Dodatkowo do widoku LinearLayout dodałem flagę "android wake lock flag" żeby ekran nie usypiał się.

W kolejnym etapie aplikacji mobilnej można się skupić na dodaniu:
 * widoku mapy
 * przeniesieniu skanowania wifi do BackgroundService

W tym momencie możemy śmiało zakończyć etap pierwszy. W etapie drugim skupię się na przekazaniu danych do serwera.
Etap drugi możemy podzielić na następujące punkty:
 * ustalenie formatu przekazywanych danych
 * zmodyfikowanie modelu WifiObservation poprzez dodanie flagi exported
 * utworzenie odpowiednich akcji i widoków w aplikacji mobilnej (konfigurowalny url serwera, przycisk do exportu)
 * możliwość usuwania wyexportowanych danych (oszczędność miejsca na urządzeniu)
 * przyjęcie przekazanych danych na serwerze rails.

## Denormalizacja danych
Ze względu na cechę badawczą pracy oraz na proste podejście do problemu, zdecydowałem się na zdenormalizowanie danych w bazie danych aplikacji mobilnej.

Dane zdenormalizowane będą zajmować więcej miejsca ale umożliwą na dokładniejsze lokalizowanie miejsca w którym jest ustawiony accesspoint.

Wszystkie dane będą zapisane w pojedyńczej tabeli. Problem jaki powstaje poprzez taki stan schematu bazy danych jest to, żeby znaleźć, które sieci wifi były znalezione w konkretnym skanie, trzeba szukać sieci o takiej samej wartości seen_at.


## Słownik
ORM
accesspoint
wifi
flaga - boolean true/false


# wstep

Skad pomysl na badanie tematu sieci wifi? Technologia ta juz rozprzestrzenila sie na calym swiecie z ogromnym sukcesem. Więc można by postawić tezę, że w tym obszarze nie czeka na nas nic interesującego. Jednak dopiero teraz gdy punkty dostępu są rozprzestrzenione na skalę masową (w pojedynczym mieszkaniu bloku w zasięgu są dziesiątki a czasem nawet setki wykrytych sieci wifi). 
