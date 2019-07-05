# JoinTitle

Simple plugin which send custom title on player loggin.

# Dependency

[Luckperms](https://ore.spongepowered.org/Luck/LuckPerms) Optional = true.

[PlaceholderAPI](https://forums.spongepowered.org/t/placeholderapi-a-hub-for-your-placeholders/16200) Optional = false.

# Commands:

>"/jt" or "/jointitle"

/jt reload - permission: jointitle.commands.admin, discription: Reloads plugin config.

/jt send <admin/vip/default> - permission: jointitle.commands.admin, discription: Send you specified title (Useful to check/edit text).

# Extra info

There is 3 sample to make custom title, admin/vip/default and 3 permission to apply on user.

Admin title - permission: jointitle.title.admin (or wildcard)

VIP title - permission: jointitle.title.vip

Default title - permission: jointitle.title.default

# Config

Default {
    # Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.
    FadeIn=101
    # Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.
    FadeOut=101
    # Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.
    Staytime=202
    # Your text need to be between :and#. For ex title:Welcome to my server#
    Text="#title:title#subtitle:subtitle#actionbar:actionbar"
}

Your text need to be between #title:/#subtitle:/#actionbar: and # supports color codes and placeholders. 

# Showcase

https://imgur.com/bKcMM6l
