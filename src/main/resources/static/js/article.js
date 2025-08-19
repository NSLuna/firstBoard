// 공통: CSRF
function csrf() {
  const token  = document.querySelector('meta[name="_csrf"]')?.content;
  const header = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';
  if (!token) console.warn('CSRF token not found in meta tags.');
  return { header, token };
}

///* ========== 1) 상세 페이지: 삭제만 처리 ========== */
//(() => {
//  const btn = document.getElementById('delete-btn');
//  if (!btn) return;
//
//  btn.addEventListener('click', async () => {
//    const id = document.getElementById('article-id')?.value;
//    if (!id) return alert('id가 없습니다.');
//    if (!confirm('정말 삭제할까요?')) return;
//
//    const { header, token } = csrf();
//    const res = await fetch(`/api/articles/${id}`, {
//      method: 'DELETE',
//      headers: token ? { [header]: token } : undefined,
//      credentials: 'same-origin'
//    });

    if (!res.ok) return alert(`삭제 실패: ${res.status}`);
    location.replace('/blog');
  });
})();

/* ========== 2) 수정 페이지: PUT 처리(수정 폼에만) ========== */
(() => {
  const btn = document.getElementById('update-btn'); // article-edit.html 에 이 버튼을 둬
  if (!btn) return;

  btn.addEventListener('click', async () => {
    const id = document.getElementById('article-id')?.value;
    const title = document.getElementById('title')?.value?.trim();
    const content = document.getElementById('content')?.value?.trim();
    if (!id) return alert('id가 없습니다.');
    if (!title || !content) return alert('제목/내용을 입력해 주세요.');

    const { header, token } = csrf();
    const res = await fetch(`/api/articles/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { [header]: token } : {})
      },
      body: JSON.stringify({ title, content }),
      credentials: 'same-origin'
    });

    if (!res.ok) return alert(`수정 실패: ${res.status}`);
    location.replace(`/articles/${id}`);
  });
})();

/* ========== 3) 새 글 작성 페이지: 서버 폼 전송 사용 ========== */
// article-form.html 은 <form method="post" action="/articles"> 로 서버에 맡김.
// create-btn 핸들러는 더 이상 사용하지 않음(중복 전송 방지).
