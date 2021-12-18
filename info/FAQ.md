# bugloos_player - Frequently Asked Questions

For a list of **supported formats** read [Supported Formats](FORMATS.md)

This FAQ will be continually updated as new changes and updates are made to bugloos_player.

## What is bugloos_player?

bugloos_player is local music player for android that I built for myself, primarily. Its designed to avoid a lot of the feature-bloat and frustrating UX that other FOSS music players have, while still remaining customizable to an extent.

## How can I contribute/report issues?

Open an [Issue](https://github.com/hamidfathi1998/Bugloos-Player/issues) or a [Pull Request](https://github.com/hamidfathi1998/Bugloos-Player/pulls), please note the [Contribution Guidelines](../.github/CONTRIBUTING.md) and [Accepted Additions](ADDITIONS.md) however.

## Why ExoPlayer?

ExoPlayer is far more flexible than the native MediaPlayer API, which allows consistent behavior across devices & OEMs amd the ability to be extended to music sources outside of local files. You can read more about the benefits (and drawbacks) of ExoPlayer [Here](https://exoplayer.dev/pros-and-cons.html).

## Why cant I have a custom accent?

Custom accents would require some changes to the accent system in the app, which will take some time. I plan to add them however.

## Why are accents lighter/less saturated in dark mode?

As per the [Material Guidelines](https://material.io/design/color/dark-theme.html), accents should be less saturated on dark mode to reduce eye strain and to make it look better in general.

You will be able to set the accent to something less saturated when I implement custom accents.

## Why isn't edge-to-edge enabled on versions below Oreo?

The APIs for changing system bar colors were only added in API Level 27 (Oreo MR1), meaning that edge-to-edge will not work below that version.

I could possibly extend edge-to-edge support to earlier versions, but it would take awhile.

## Why doesn't edge-to-edge work when my phone is in landscape?

When a *phone* [Not a tablet] is in landscape, the insets meant to make the UI fit don't work properly, therefore the feature is mostly disabled.

## Does this app keep/send any information about myself or my device?

bugloos_player does not log any information about the device or its owner, and it has no internet access to send that information off in the first place.