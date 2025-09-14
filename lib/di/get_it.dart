import 'package:dev_ease/controller/glance_widget_controller.dart';
import 'package:get_it/get_it.dart';

final GetIt getIt = GetIt.instance;
void setupGetIt() {
  getIt.registerSingleton<GlanceWidgetController>(GlanceWidgetController());
}
