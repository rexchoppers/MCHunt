# MCHunt Changelog

## [1.0.0-alpha.7] - ?
### Removed
- Removed old command executor (Not required + saves MB of build size)
### Added
- Added message to player when they leave arena
### Fixed
- Fixed PALE_OAK_WALL_HANGING_SIGN and PALE_OAK_WALL_SIGN not being excluded (Thank you @LeDOC666)
- Fixed missing locale throwing exception instead of defaulting to English (Thank you @LeDOC666)

## [1.0.0-alpha.6] - 2025-09-24
### Added
- Added game length configuration
- Added hider still time (Time hiders stay still before disguising)
### Fixed
- Fixed hider message spam
- Fixed hider moved message being sent after game end

## [1.0.0-alpha.5] - 2025-08-28
### Added
- Added in-game menu using /mchunt
### Fixed
- Rouge testing line starting game with only 1 player

## [1.0.0-alpha.4] - 2025-08-26
### Updated
- Updated localization manager to singleton pattern
### Removed
- Removed debug server log