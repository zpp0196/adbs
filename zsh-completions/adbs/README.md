# adbs autocomplete plugin

Adds autocomplete options for adbs commands.

To enable it, add `adbs` to your plugins array:

```zsh
# ~/.zshrc
plugins=(... adbs)
```

And copy it to the zsh plugins directory:

```bash
ln -snf $ADBS_ROOT/zsh-completions/adbs $ZSH/custom/plugins/adbs
```
