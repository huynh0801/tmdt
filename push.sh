#!/bin/bash

echo "=========================================="
echo "Git Push Helper Script"
echo "=========================================="
echo ""

# Kiểm tra có commit chưa push không
COMMITS_TO_PUSH=$(git log --oneline origin/master..HEAD | wc -l)

if [ "$COMMITS_TO_PUSH" -eq 0 ]; then
    echo "✅ No commits to push. Everything is up to date!"
    exit 0
fi

echo "📦 Commits to push: $COMMITS_TO_PUSH"
echo ""
git log --oneline origin/master..HEAD
echo ""

# Kiểm tra remote
echo "🔗 Remote repository:"
git remote -v | grep push
echo ""

# Hỏi user có muốn push không
read -p "Do you want to push now? (y/n): " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🚀 Pushing to origin master..."
    git push -u origin master
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ Push successful!"
    else
        echo ""
        echo "❌ Push failed!"
        echo ""
        echo "Common solutions:"
        echo "1. Use Personal Access Token:"
        echo "   git remote set-url origin https://<TOKEN>@github.com/huynh0801/tmdt.git"
        echo ""
        echo "2. Use SSH:"
        echo "   git remote set-url origin git@github.com:huynh0801/tmdt.git"
        echo ""
        echo "3. Check PUSH_TO_GITHUB.md for detailed instructions"
    fi
else
    echo "❌ Push cancelled"
fi
