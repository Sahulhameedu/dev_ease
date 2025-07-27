import 'package:flutter/material.dart';

class SettingTile extends StatelessWidget {
  final IconData leadingIcon;
  final String title;
  final bool isEnabled;
  final Function() onTap;
  const SettingTile({
    super.key,
    required this.leadingIcon,
    required this.title,
    required this.isEnabled,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: Colors.grey),
        ),
        child: ListTile(
          leading: Icon(leadingIcon),
          title: Text(title),
          trailing: AnimatedContainer(
            duration: Duration(milliseconds: 200),
            height: 25,
            width: 45,
            alignment: isEnabled ? Alignment.centerRight : Alignment.centerLeft,
            decoration: BoxDecoration(
              color: isEnabled
                  ? Colors.deepPurple.withValues(alpha: .5)
                  : Colors.grey.withValues(alpha: .5),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: Colors.grey),
            ),
            child: Container(
              height: 20,
              width: 20,
              margin: EdgeInsets.symmetric(horizontal: 1),
              decoration: BoxDecoration(
                color: isEnabled ? Colors.deepPurple : Colors.grey,
                borderRadius: BorderRadius.circular(50),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
