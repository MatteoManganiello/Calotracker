import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DailyGoalPage extends StatefulWidget {
  const DailyGoalPage({super.key});
  static const route = 'dailyGoal';

  @override
  State<DailyGoalPage> createState() => _DailyGoalPageState();
}

class _DailyGoalPageState extends State<DailyGoalPage> {
  static const _prefKey = 'daily_calorie_goal';
  static const _brand = Color(0xFFA5D6A7);
  static const _min = 800, _max = 5000, _step = 50;

  final _controller = TextEditingController();
  int? _original;
  int _goal = 2000;
  bool _loading = true;

  @override
  void initState() {
    super.initState();
    SharedPreferences.getInstance().then((p) {
      final saved = p.getInt(_prefKey);
      setState(() {
        _original = saved;
        _goal = saved ?? 2000;
        _controller.text = _goal.toString();
        _loading = false;
      });
    });
  }

  void _setGoal(int v) {
    _goal = v.clamp(_min, _max);
    _controller.text = _goal.toString();
    setState(() {});
  }

  Future<void> _save() async {
    final p = await SharedPreferences.getInstance();
    await p.setInt(_prefKey, _goal);
    if (!mounted) return;
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Obiettivo impostato a $_goal kcal')),
    );
    Navigator.pop(context, _goal);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    final canSave = _goal >= _min && _goal <= _max && _goal != _original;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Imposta obiettivo giornaliero'),
        backgroundColor: _brand,
        foregroundColor: Colors.white,
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.fromLTRB(16, 16, 16, 120),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              if (_original != null)
                Text('Obiettivo attuale: $_original kcal',
                    style: const TextStyle(fontWeight: FontWeight.w600)),
              const SizedBox(height: 12),

              // – [ TextField ] +
              Row(children: [
                IconButton.filled(
                  onPressed: () => _setGoal(_goal - _step),
                  style: IconButton.styleFrom(backgroundColor: _brand),
                  icon: const Icon(Icons.remove, color: Colors.white),
                ),
                const SizedBox(width: 6),
                Expanded(
                  child: TextField(
                    controller: _controller,
                    textAlign: TextAlign.center,
                    keyboardType: TextInputType.number,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    decoration: InputDecoration(
                      hintText: 'Es. 2000',
                      suffixText: 'kcal',
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    onChanged: (v) {
                      final n = int.tryParse(v);
                      if (n != null) _setGoal(n);
                    },
                    onSubmitted: (_) => canSave ? _save() : null,
                  ),
                ),
                const SizedBox(width: 8),
                IconButton.filled(
                  onPressed: () => _setGoal(_goal + _step),
                  style: IconButton.styleFrom(backgroundColor: _brand),
                  icon: const Icon(Icons.add, color: Colors.white),
                ),
              ]),

              const SizedBox(height: 12),

              // Slider
              Slider(
                value: _goal.toDouble(),
                min: _min.toDouble(),
                max: _max.toDouble(),
                divisions: (_max - _min) ~/ _step,
                label: '$_goal kcal',
                activeColor: _brand,
                onChanged: (v) => _setGoal(v.round()),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [Text('$_min'), Text('$_max')],
              ),

              const SizedBox(height: 12),

              const SizedBox(height: 16),

              // Card esplicativa (motivazioni calorie più alte)
              Card(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                  side: BorderSide(color: _brand.withOpacity(0.25)),
                ),
                elevation: 0,
                child: Padding(
                  padding: const EdgeInsets.all(14),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(children: [
                        Icon(Icons.info_outline, color: _brand),
                        const SizedBox(width: 8),
                        const Text(
                          'Perché questo obiettivo?',
                          style: TextStyle(
                            fontSize: 16, fontWeight: FontWeight.w700),
                        ),
                      ]),
                      const SizedBox(height: 8),
                      const Text('Motivi comuni per assumere più calorie:'),
                      const SizedBox(height: 6),
                      const _Bullet('Fase di aumento massa muscolare (bulk).'),
                      const _Bullet('Allenamento intenso o giornata molto attiva.'),
                      const _Bullet('Recupero da infortunio o malattia.'),
                      const _Bullet('Metabolismo basale elevato.'),
                      const _Bullet('Obiettivo di aumento peso controllato.'),
                      const SizedBox(height: 6),
                      const Text(
                        'Suggerimento: distribuisci le calorie in pasti bilanciati '
                        '(proteine, carboidrati, grassi e fibre).',
                        style: TextStyle(fontStyle: FontStyle.italic),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),

      // CTA in basso
      bottomNavigationBar: SafeArea(
        minimum: const EdgeInsets.fromLTRB(16, 0, 16, 16),
        child: Row(children: [
          Expanded(
            child: OutlinedButton(
              onPressed: () => Navigator.pop(context),
              style: OutlinedButton.styleFrom(
                side: const BorderSide(color: _brand, width: 2),
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12)),
                foregroundColor: _brand,
              ),
              child: const Text('Annulla'),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: ElevatedButton(
              onPressed: canSave ? _save : null,
              style: ElevatedButton.styleFrom(
                backgroundColor: _brand,
                foregroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12)),
              ),
              child: const Text('Salva'),
            ),
          ),
        ]),
      ),
    );
  }
}

// Mini widget per punti elenco
class _Bullet extends StatelessWidget {
  final String text;
  const _Bullet(this.text);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 2, bottom: 4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('•  '),
          Expanded(child: Text(text)),
        ],
      ),
    );
  }
}
