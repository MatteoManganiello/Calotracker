import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'homepages/DailyGoal.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  static const _prefKey = 'daily_calorie_goal';
  int? _dailyGoal;

  @override
  void initState() {
    super.initState();
    _loadDailyGoal();
  }

  Future<void> _loadDailyGoal() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      _dailyGoal = prefs.getInt(_prefKey);
    });
  }

  Future<void> _openDailyGoalPage() async {
    final result = await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const DailyGoalPage()),
    );

    if (result is int) {
      setState(() {
        _dailyGoal = result;
      });
    } else {
      // Ricarica comunque dal persistence in caso di salvataggio senza result.
      await _loadDailyGoal();
    }
  }

  @override
  Widget build(BuildContext context) {
    final hasGoal = _dailyGoal != null;

    // Testo titolo della card obiettivo
    const goalTitle = 'Obiettivo giornaliero';

    // Trailing: SOLO qui mettiamo una card bianca con il numero
    final Widget? goalTrailing = hasGoal
        ? Card(
            color: Colors.white,
            elevation: 2,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              child: Text(
                '${_dailyGoal!} kcal',
                style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w700,
                  color: Colors.black,
                ),
              ),
            ),
          )
        : null;

    return Scaffold(
      appBar: AppBar(title: const Text('Home')),
      body: Padding(
        padding: const EdgeInsets.all(10),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _ColoredCard(
                cardName: goalTitle,
                imagePath: 'assets/images/add.png',
                color: Colors.green[200],
                width: double.infinity,
                height: 60,
                onTap: _openDailyGoalPage, 
                trailing: goalTrailing,    
              ),
              if (!hasGoal) ...[
                const SizedBox(height: 8),

                GestureDetector(
                  onTap: _openDailyGoalPage,
                  child: Row(
                    children: const [
                      Icon(Icons.info_outline, size: 18),
                      SizedBox(width: 6),
                      Text(
                        'Tocca la card per impostare l\'obiettivo',
                        style: TextStyle(fontSize: 12),
                      ),
                    ],
                  ),
                ),
              ],
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: _ColoredCard(
                      cardName: 'Calorie giornaliere',
                      imagePath: 'assets/images/Fuoco.png',
                      color: Colors.orange[200],
                      height: 225,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: _ColoredCard(
                      cardName: 'Attività fisica',
                      imagePath: 'assets/images/Muscolo.png',
                      color: Colors.blue[200],
                      height: 225,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 24),
              const Text(
                'Blogs',
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 12),
              SizedBox(
                height: 350,
                child: ListView(
                  scrollDirection: Axis.horizontal,
                  children: const [
                    _HorizontalCard(
                      title: 'Ricetta proteica',
                      subtitle:
                          'In un mondo sempre più frenetico e dominato da abitudini sedentarie, '
                          'prendersi cura del proprio corpo e della propria mente è diventato fondamentale. '
                          'L’attività fisica regolare, abbinata a un’alimentazione equilibrata, è un investimento '
                          'sulla qualità della vita. Sport e buona alimentazione lavorano in sinergia per migliorare '
                          'il benessere fisico, mentale ed emotivo.',
                      imagePath: 'assets/images/Alimentazione.png',
                    ),
                    SizedBox(width: 12),
                    _HorizontalCard(
                      title: 'Consiglio nutrizionale',
                      subtitle:
                          'Il termine cibo spazzatura (dall’inglese junk food) si riferisce a tutti quegli alimenti '
                          'ad alta densità calorica ma poveri di valore nutrizionale. Si tratta per lo più di prodotti '
                          'industriali altamente processati, ricchi di zuccheri raffinati, grassi saturi, sale e additivi '
                          'chimici, ma estremamente poveri di vitamine, minerali, fibre e proteine di qualità. Nonostante ciò, '
                          'il cibo spazzatura è oggi una delle opzioni alimentari più diffuse, soprattutto tra bambini, '
                          'adolescenti e giovani adulti, grazie alla sua facile reperibilità, al basso costo e al sapore gratificante.',
                      imagePath: 'assets/images/CiboSpazzatura.png',
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _ColoredCard extends StatelessWidget {
  final String cardName;
  final String imagePath;
  final Color? color;
  final double? width;
  final double height;
  final VoidCallback? onTap;

  /// NUOVO: widget opzionale mostrato a destra (es. la card bianca col numero)
  final Widget? trailing;

  const _ColoredCard({
    required this.cardName,
    required this.imagePath,
    this.color,
    this.width,
    this.height = 100,
    this.onTap,
    this.trailing,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: width,
      child: Card(
        color: color,
        clipBehavior: Clip.antiAlias,
        child: _SampleCard(
          cardName: cardName,
          imagePath: imagePath,
          height: height,
          onTap: onTap,
          trailing: trailing, // passa il trailing al contenuto
        ),
      ),
    );
  }
}

class _SampleCard extends StatelessWidget {
  final String cardName;
  final String imagePath;
  final double height;
  final VoidCallback? onTap;

  /// NUOVO: trailing opzionale (es. la card bianca col numero)
  final Widget? trailing;

  const _SampleCard({
    required this.cardName,
    required this.imagePath,
    required this.height,
    this.onTap,
    this.trailing,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap ??
          () {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text('Hai cliccato "$cardName"')),
            );
          },
      child: SizedBox(
        height: height,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 12.0),
          child: Row(
            children: [
              Image.asset(
                imagePath,
                width: 30,
                height: 30,
                fit: BoxFit.contain,
                errorBuilder: (context, error, stackTrace) =>
                    const Icon(Icons.error),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  cardName,
                  style: const TextStyle(fontSize: 20),
                  overflow: TextOverflow.ellipsis,
                ),
              ),
              if (trailing != null) ...[
                const SizedBox(width: 8),
                trailing!,
              ],
            ],
          ),
        ),
      ),
    );
  }
}

class _HorizontalCard extends StatelessWidget {
  final String title;
  final String subtitle;
  final String imagePath;

  const _HorizontalCard({
    required this.title,
    required this.subtitle,
    required this.imagePath,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: 260,
      child: Card(
        elevation: 4,
        color: Colors.grey[200],
        clipBehavior: Clip.antiAlias,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Image.asset(
              imagePath,
              width: 255,
              height: 220,
              fit: BoxFit.cover,
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style:
                          const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Expanded(
                      child: SingleChildScrollView(
                        child: Text(
                          subtitle,
                          style: const TextStyle(fontSize: 14),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

