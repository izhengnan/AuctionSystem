// 封装fetch请求，统一处理前后端通信
const BASE_URL = 'http://localhost:8080';

// 请求配置
const config = {
    timeout: 10000, // 10秒超时
};

// 统一处理响应
async function handleResponse(response) {
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    } else {
        return await response.text();
    }
}

// 通用GET请求
export async function get(url, params = {}) {
    // 构建查询参数
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${BASE_URL}${url}?${queryString}` : `${BASE_URL}${url}`;
    
    const response = await fetch(fullUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'token': localStorage.getItem('token') || ''
        }
    });
    
    return handleResponse(response);
}

// 通用POST请求
export async function post(url, data = {}) {
    const response = await fetch(`${BASE_URL}${url}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'token': localStorage.getItem('token') || ''
        },
        body: JSON.stringify(data)
    });
    
    return handleResponse(response);
}

// 通用PUT请求
export async function put(url, data = {}) {
    const response = await fetch(`${BASE_URL}${url}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'token': localStorage.getItem('token') || ''
        },
        body: JSON.stringify(data)
    });
    
    return handleResponse(response);
}

// 通用DELETE请求
export async function del(url, params = {}) {
    // 构建查询参数
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${BASE_URL}${url}?${queryString}` : `${BASE_URL}${url}`;
    
    const response = await fetch(fullUrl, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'token': localStorage.getItem('token') || ''
        }
    });
    
    return handleResponse(response);
}

// 存储token
export function setToken(token) {
    localStorage.setItem('token', token);
}

// 获取token
export function getToken() {
    return localStorage.getItem('token');
}

// 清除token
export function removeToken() {
    localStorage.removeItem('token');
}

// 格式化日期时间
export function formatDateTime(dateString) {
    const date = new Date(dateString);
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0') + ' ' + 
           String(date.getHours()).padStart(2, '0') + ':' + 
           String(date.getMinutes()).padStart(2, '0');
}

// 格式化货币
export function formatCurrency(amount) {
    return '¥' + parseFloat(amount).toFixed(2);
}